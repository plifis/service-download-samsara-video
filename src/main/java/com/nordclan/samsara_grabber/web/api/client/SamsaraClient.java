package com.nordclan.samsara_grabber.web.api.client;

import com.nordclan.samsara_grabber.domain.events.safety.SafetyEventsList;
import com.nordclan.samsara_grabber.domain.page.SamsaraPage;
import com.nordclan.samsara_grabber.domain.tachograph.TachographList;
import com.nordclan.samsara_grabber.domain.trip.TripList;
import com.nordclan.samsara_grabber.domain.vehicle.VehiclesList;
import com.nordclan.samsara_grabber.web.api.constants.Urls;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.nonNull;

@Data
@Component
@Slf4j
public class SamsaraClient {
    private final RestTemplate restTemplate;


    public VehiclesList getAllVehicles(SamsaraPage page) {
        VehiclesList resp = new VehiclesList();
        try {
            if (nonNull(page.getHasNextPage())) {
                resp = restTemplate.getForObject(Urls.Vehicles.FULL + page.getEndCursor(), VehiclesList.class);

            } else {
                resp = restTemplate.getForObject(Urls.Vehicles.FULL, VehiclesList.class);
            }

        } catch (Exception e) {
            log.error(String.format("Error for get list of vehicles: endpoint - %s, message - %s",
                    Urls.Vehicles.FULL, e.getMessage()));
        }
        return resp;
    }

    public TripList getVehicleTrips(@NonNull Long vehicleId, @NonNull Long startMs, @NonNull Long endMs) {
        String url = Urls.Trips.FULL + "?vehicleId=" +
                vehicleId + "&startMs=" +
                startMs + "&endMs=" + endMs;
        return restTemplate.getForObject(url, TripList.class);
    }

    public TachographList getTachographVehicleFiles(String vehicleIds, @NonNull String startTime, @NonNull String endTime) {
        String url = Urls.Vehicles.Tachograph.FULL +
                "?startTime=" + startTime +
                "&endTime=" + endTime +
                "&vehicleIds=" + vehicleIds;

        return restTemplate.getForObject(url, TachographList.class);
    }

    public SafetyEventsList getAllSafetyEvents(@NonNull String startTime, @NonNull String endTime, SamsaraPage page) {
        String url;
        if (nonNull(page.getHasNextPage())) {
            url = Urls.SafetyEvents.FULL + "?after=" + page.getEndCursor() +
                    "&startTime=" + encodeValue(startTime) +
                    "&endTime=" + encodeValue(endTime);
        } else {
            url = Urls.SafetyEvents.FULL +
                    "?startTime=" + startTime +
                    "&endTime=" + endTime;
        }
        log.info("Send request to URL: " + url);
        return restTemplate.getForObject(url, SafetyEventsList.class);
    }

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            log.error("Error encode " + value);
            throw new RuntimeException(e.getCause());
        }
    }
}