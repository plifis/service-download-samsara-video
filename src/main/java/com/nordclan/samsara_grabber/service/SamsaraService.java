package com.nordclan.samsara_grabber.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nordclan.samsara_grabber.domain.behaviorlabels.BehaviorLabels;
import com.nordclan.samsara_grabber.domain.events.safety.SafetyEventsDto;
import com.nordclan.samsara_grabber.domain.events.safety.SafetyEventsList;
import com.nordclan.samsara_grabber.domain.page.SamsaraPage;
import com.nordclan.samsara_grabber.domain.page.SamsaraPageImpl;
import com.nordclan.samsara_grabber.domain.tachograph.TachographList;
import com.nordclan.samsara_grabber.domain.tachograph.TachographResponseDto;
import com.nordclan.samsara_grabber.domain.trip.Trip;
import com.nordclan.samsara_grabber.domain.vehicle.VehiclesDto;
import com.nordclan.samsara_grabber.domain.vehicle.VehiclesList;
import com.nordclan.samsara_grabber.exceptions.ListIdsEmptyException;
import com.nordclan.samsara_grabber.exceptions.TimeRangeException;
import com.nordclan.samsara_grabber.web.api.client.FileDownloadClient;
import com.nordclan.samsara_grabber.web.api.client.SamsaraClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Data
@Service
@Slf4j
public class SamsaraService implements ISamsaraService {
    /**
     * Limited to a 90 day window with respect to startMs and endMs
     * 90 * 24 * 60 * 60 * 1000
     */
    private static final Long maxRange = 7776000000L;

    @Value("${env.location}")
    private String ENV_LOCATION;

    @Value("${video.location}")
    private String VIDEO_LOCATION;

    @Value("${start.timerange}")
    private String START_TIME;

    @Value("${end.timerange}")
    private String END_TIME;

    @Value("${limit.entity.repsponse}")
    private Integer LIMIT_RESPONSE_ENTITY;

    private String SEP = "-";

    private String FORWARD = "Forward/%s/%s";

    private String INWARD = "Inward/%s/%s";

    private String TRACKEDINWARD = "TrackedInward/%s/%s";
    private String FILENAME_MP4 = "%s.mp4";

    private final ObjectMapper objectMapper;

    private final SamsaraClient samsaraClient;
    private final FileDownloadClient fileDownloadClient;

    /**
     * Загружает все автомобили из системы
     */
    @Override
    public void loadAllVehicles() {
        SamsaraPageImpl page = new SamsaraPageImpl();
        List<VehiclesDto> vehiclesDto = new ArrayList<>();
        try {

            VehiclesList resp = getAllVehicles(page);
            vehiclesDto.addAll(resp.getData());
            log.info("Get vehicles " + resp.getData()
                    .size());

            while (resp.getPagination()
                    .getHasNextPage()) {

                page.setEndCursor(resp.getPagination()
                        .getEndCursor());

                resp = getAllVehicles(page);
                vehiclesDto.addAll(resp.getData());
                log.info("Get vehicles " + resp.getData()
                        .size());

                if (resp.getData()
                        .size() >= 512) {
                    writeResponse(vehiclesDto);
                    vehiclesDto.clear();
                }
            }

            writeResponse(vehiclesDto);

        } catch (Exception e) {
            log.info("Exception when get load get vehicles.");
        }
    }

    /**
     * Загружает поездки для указанного автомобиля по ИД и за временной интервал
     *
     * @throws IOException
     * @throws ListIdsEmptyException
     */
    @Override
    public void loadAllVehicleTrips() throws IOException, ListIdsEmptyException {
        List<Long> ids = loadListVehiclesIds();

        if (ids.isEmpty()) {
            throw new ListIdsEmptyException("List ids don't must empty.");
        }

        Map<String, Long> timeRange = loadTimeRange().entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        s -> Timestamp.valueOf(s.getValue())
                                .getTime()));


        if (timeRange.get("end") - timeRange.get("start") > maxRange) {
            throw new TimeRangeException("Range is bigger 90 days.");
        }
        for (Long id : ids) {

            List<Trip> tripsVehicle = samsaraClient.getVehicleTrips(id, timeRange.get("start"), timeRange.get("end"))
                    .getTrips();

            writeResponse(tripsVehicle);
        }

    }

    /**
     * Загружает данные тахографа автомобиля с ссылкой на видео
     */
    @Override
    public void loadTachographVehicleFiles() throws IOException {
        SamsaraPageImpl page = new SamsaraPageImpl();
        List<TachographResponseDto> tachographResponseDto = new ArrayList<>();
        List<Long> ids = loadListVehiclesIds();
        Map<String, String> timeRange = loadTimeRange();

        for (Long id : ids) {
            TachographList resp = samsaraClient.getTachographVehicleFiles(
                    String.valueOf(id), timeRange.get("start"), timeRange.get("end"));

            tachographResponseDto.addAll(resp.getData());

            while (resp.getPagination()
                    .getHasNextPage()) {

                page.setEndCursor(resp.getPagination()
                        .getEndCursor());

                resp = samsaraClient.getTachographVehicleFiles(
                        String.valueOf(id), timeRange.get("start"), timeRange.get("end"));

                tachographResponseDto.addAll(resp.getData());
            }
        }

        writeResponse(tachographResponseDto);

        loadVideoTachographFiles(tachographResponseDto);
    }

    /**
     * Загружает все видео для всех автомобилей из ответа системы
     *
     * @param tachographResponseDto ответ системы с ссылками на видео и информацией о видео и автомобиле
     */
    public void loadVideoTachographFiles(List<TachographResponseDto> tachographResponseDto) {
        for (TachographResponseDto tachographResponseVehicle : tachographResponseDto) {

            tachographResponseVehicle.getFiles()
                    .forEach(f -> {
                        String fileName = String.format("%s_%s.ddd", f.getVehicleIdentificationNumber(), f.getCreatedAtTime()
                                .substring(0, 10));
                        Path video = fileDownloadClient.loadVideoFile(f.getUrl());
                        Map<String, String> envs = new HashMap<>();
                        envs.put("pathToFile", "/tachographs");
                        envs.put("fileName", fileName);
                        writeVideo(video, envs);
                    });
        }
    }

    /**
     * Копирует временный файл, созданный контроллером в требуемую директорию и удаляет временный файл
     *
     * @param file Временный видео файл
     * @param envs переменные для формирования строки
     */
    private void writeVideo(Path file, Map<String, String> envs) {
        String destination = String.format("%s/%s/", VIDEO_LOCATION, envs.get("pathToFile"));
        try {
            Files.createDirectories(Path.of(destination));

            Files.copy(file, Path.of(destination + "/" + envs.get("fileName")), StandardCopyOption.REPLACE_EXISTING);
            Files.delete(file);

        } catch (Exception e) {
            log.error("Cannot create or write to the file, it may already exist." + e.getMessage());
        }
    }

    /**
     * Записать список сущностей в файл
     *
     * @param response Ответ - список объектов полученных из системы
     * @param <T>      тип объекта ответа системы
     * @throws IOException проблемы записи в файл (не может создать и т.д.)
     */
    @Override
    public <T> void writeResponse(List<T> response) throws IOException {
        String fileName = String.format("%s_%s.json", response.get(0)
                .getClass()
                .getSimpleName(), LocalDate.now());
        log.info("File writing process started " + fileName);
        File file = new File(fileName);
        objectMapper.writeValue(file, response);
    }

    @Override
    public void getAllSafetyEvents() {
        SamsaraPageImpl page = new SamsaraPageImpl();
        int size;
        Map<String, String> timeRange = loadTimeRange();

        SafetyEventsList resp = samsaraClient.getAllSafetyEvents(timeRange.get("start"), timeRange.get("end"), page);
        size = resp.getData().size();

        while (resp.getPagination()
                .getHasNextPage()) {

            loadVideoSafetyEventVideoFile(resp.getData());

            page.setEndCursor(resp.getPagination()
                    .getEndCursor());

            resp = samsaraClient.getAllSafetyEvents(timeRange.get("start"), timeRange.get("end"), page);
            size = size + resp.getData().size();
        }

        loadVideoSafetyEventVideoFile(resp.getData());
        log.info("Download files of safety events is complete!" + size);
    }

    public void loadVideoSafetyEventVideoFile(List<SafetyEventsDto> safetyEventsDto) {
        safetyEventsDto.stream()
                .filter(s -> Optional.ofNullable(s.getDownloadForwardVideoUrl())
                        .isPresent())
                .forEach(s -> {
                    Path video = fileDownloadClient.loadVideoFile(s.getDownloadForwardVideoUrl());
                    writeVideo(video, getPathEnvs(s, FORWARD));
                });

        safetyEventsDto.stream()
                .filter(s -> Optional.ofNullable(s.getDownloadInwardVideoUrl())
                        .isPresent())
                .forEach(s -> {
                    Path video = fileDownloadClient.loadVideoFile(s.getDownloadInwardVideoUrl());
                    writeVideo(video, getPathEnvs(s, INWARD));
                });

        safetyEventsDto.stream()
                .filter(s -> Optional.ofNullable(s.getDownloadTrackedInwardVideoUrl())
                        .isPresent())
                .forEach(s -> {
                    Path video = fileDownloadClient.loadVideoFile(s.getDownloadTrackedInwardVideoUrl());
                    writeVideo(video, getPathEnvs(s, TRACKEDINWARD));
                });
    }

    private Map<String, String> getPathEnvs(SafetyEventsDto safetyEventsDto, String pathToFile) {
        Map<String, String> envs = new HashMap<>();



        String labels = safetyEventsDto.getBehaviorLabels()
                .stream()
                .map(BehaviorLabels::getLabel)
                .collect(Collectors.joining(SEP));

        envs.put("pathToFile", String.format(pathToFile, labels, safetyEventsDto.getTime()
                .substring(0, 10)));

        envs.put("fileName", String.format(FILENAME_MP4, safetyEventsDto.getId()));

        return envs;
    }


    private VehiclesList getAllVehicles(SamsaraPage page) {
        return samsaraClient.getAllVehicles(page);
    }

    private List<Long> loadListVehiclesIds() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ENV_LOCATION))) {
            String str = reader.readLine();
            return Arrays.stream(
                            str.substring(str.lastIndexOf(":") + 1, str.lastIndexOf(";") - 1)
                                    .split(","))
                    .map(Long::valueOf)
                    .toList();
        } catch (Exception e) {
            log.info("Error when get ids " + e.getMessage());

            //УБРАТЬ после теста на aws
            return List.of(844424930177055L, 844424930177053L);
        }
    }

    /**
     * Загружает временные промежутки из файла
     * в файле данные представлены в виде:
     * start:2023-04-05 12:30:00;
     * end:2023-04-05 12:59:00;
     *
     * @return Map содержащая ключ - start или end, значение - строка со временем в формате "2023-04-05T12:30:00"
     */
    private Map<String, String> loadTimeRange() {
        Map<String, String> timeRange = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ENV_LOCATION))) {
            List<String> strRanges = reader.lines()
                    .filter(s -> !s.contains("vehicleId"))
                    .toList();

            strRanges.forEach(s -> {
                timeRange.put(s.substring(0, s.indexOf(":")), s.substring(s.indexOf(":") + 1, s.lastIndexOf(";") - 1));
            });
            return timeRange;
        } catch (Exception e) {
            log.error(" Can't find file with Time Range - " + ENV_LOCATION);

        }
        timeRange.put("start", START_TIME);
        timeRange.put("end", END_TIME);
        return timeRange;
    }
}