package com.nordclan.samsara_grabber.web.api.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nordclan.samsara_grabber.configuration.TestAppConfig;
import com.nordclan.samsara_grabber.domain.fileinfo.FileInfo;
import com.nordclan.samsara_grabber.domain.tachograph.TachographList;
import com.nordclan.samsara_grabber.domain.tachograph.TachographResponseDto;
import com.nordclan.samsara_grabber.domain.tachograph.VehicleTachographDto;
import com.nordclan.samsara_grabber.domain.vehicle.VehiclesList;
import com.nordclan.samsara_grabber.service.SamsaraService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = TestAppConfig.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "/application.properties")
@AutoConfigureMockMvc

class SamsaraClientTest {

    @Value("${env.location}")
    private String ENV_LOCATION;

    @Value("${video.location}")
    private String VIDEO_LOCATION;

    private final String vehiclesJson = "{\n" +
            "  \"data\": [\n" +
            "    {\n" +
            "      \"attributes\": [\n" +
            "        {\n" +
            "          \"id\": \"494123\",\n" +
            "          \"name\": \"Compliance/ELD\",\n" +
            "          \"numberValues\": [\n" +
            "            867,\n" +
            "            5309\n" +
            "          ],\n" +
            "          \"stringValues\": [\n" +
            "            \"HQ\",\n" +
            "            \"Leased\"\n" +
            "          ]\n" +
            "        }\n" +
            "      ],\n" +
            "      \"auxInputType1\": \"boom\",\n" +
            "      \"auxInputType10\": \"boom\",\n" +
            "      \"auxInputType11\": \"boom\",\n" +
            "      \"auxInputType12\": \"boom\",\n" +
            "      \"auxInputType13\": \"boom\",\n" +
            "      \"auxInputType2\": \"boom\",\n" +
            "      \"auxInputType3\": \"boom\",\n" +
            "      \"auxInputType4\": \"boom\",\n" +
            "      \"auxInputType5\": \"boom\",\n" +
            "      \"auxInputType6\": \"boom\",\n" +
            "      \"auxInputType7\": \"boom\",\n" +
            "      \"auxInputType8\": \"boom\",\n" +
            "      \"auxInputType9\": \"boom\",\n" +
            "      \"cameraSerial\": \"CNCK-VT8-XA8\",\n" +
            "      \"createdAtTime\": \"1991-10-12T09:54:59Z\",\n" +
            "      \"esn\": \"56349811\",\n" +
            "      \"externalIds\": {\n" +
            "        \"additionalProp\": \"string\"\n" +
            "      },\n" +
            "      \"gateway\": {\n" +
            "        \"model\": \"VG34\",\n" +
            "        \"serial\": \"GFRV-43N-VGX\"\n" +
            "      },\n" +
            "      \"harshAccelerationSettingType\": \"off\",\n" +
            "      \"id\": \"494123\",\n" +
            "      \"licensePlate\": \"6SAM123\",\n" +
            "      \"make\": \"Ford\",\n" +
            "      \"model\": \"F150\",\n" +
            "      \"name\": \"Fleet Truck #1\",\n" +
            "      \"notes\": \"These are notes about this given vehicle.\",\n" +
            "      \"sensorConfiguration\": {\n" +
            "        \"areas\": [\n" +
            "          {\n" +
            "            \"cargoSensors\": [\n" +
            "              {\n" +
            "                \"id\": \"12345\",\n" +
            "                \"mac\": \"00:00:5e:00:53:af\",\n" +
            "                \"name\": \"Rear temperature sensor\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"humiditySensors\": [\n" +
            "              {\n" +
            "                \"id\": \"12345\",\n" +
            "                \"mac\": \"00:00:5e:00:53:af\",\n" +
            "                \"name\": \"Rear temperature sensor\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"position\": \"back\",\n" +
            "            \"temperatureSensors\": [\n" +
            "              {\n" +
            "                \"id\": \"12345\",\n" +
            "                \"mac\": \"00:00:5e:00:53:af\",\n" +
            "                \"name\": \"Rear temperature sensor\"\n" +
            "              }\n" +
            "            ]\n" +
            "          }\n" +
            "        ],\n" +
            "        \"doors\": [\n" +
            "          {\n" +
            "            \"position\": \"back\",\n" +
            "            \"sensor\": {\n" +
            "              \"id\": \"12345\",\n" +
            "              \"mac\": \"00:00:5e:00:53:af\",\n" +
            "              \"name\": \"Rear temperature sensor\"\n" +
            "            }\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      \"serial\": \"VG12345\",\n" +
            "      \"staticAssignedDriver\": {\n" +
            "        \"id\": \"0987\",\n" +
            "        \"name\": \"Driver Name\"\n" +
            "      },\n" +
            "      \"tags\": [\n" +
            "        {\n" +
            "          \"id\": \"3914\",\n" +
            "          \"name\": \"East Coast\",\n" +
            "          \"parentTagId\": \"4815\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"updatedAtTime\": \"1970-10-08T02:11:31Z\",\n" +
            "      \"vehicleRegulationMode\": \"regulated\",\n" +
            "      \"vin\": \"1GBJ6P1B2HV112765\",\n" +
            "      \"year\": \"2008\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"pagination\": {\n" +
            "    \"endCursor\": \"MjkY\",\n" +
            "    \"hasNextPage\": false\n" +
            "  }\n" +
            "}";

    private final String tachographResponseJson = "{\n" +
            "  \"data\": [\n" +
            "    {\n" +
            "      \"files\": [\n" +
            "        {\n" +
            "          \"createdAtTime\": \"2020-01-02T15:04:05Z07:00\",\n" +
            "          \"id\": \"4aff772c-a7bb-45e6-8e41-6a53e34feb83\",\n" +
            "          \"url\": \"https://samsara-tachograph-files.s3.us-west-2.amazonaws.com/123/456/789/4aff772c-a7bb-45e6-8e41-6a53e34feb83.ddd\",\n" +
            "          \"vehicleIdentificationNumber\": \"1000000492436002\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"vehicle\": {\n" +
            "        \"ExternalIds\": {\n" +
            "          \"maintenanceId\": \"250020\",\n" +
            "          \"payrollId\": \"ABFS18600\"\n" +
            "        },\n" +
            "        \"id\": \"123456789\",\n" +
            "        \"name\": \"Midwest Truck #4\"\n" +
            "      }\n" +
            "    }\n" +
            "  ],\n" +
            "  \"pagination\": {\n" +
            "    \"endCursor\": \"MjkY\",\n" +
            "    \"hasNextPage\": false\n" +
            "  }\n" +
            "}";

    private final String tripsJson = "{\n" +
            "  \"trips\": [\n" +
            "    {\n" +
            "      \"assetIds\": [\n" +
            "        122\n" +
            "      ],\n" +
            "      \"codriverIds\": [\n" +
            "        122\n" +
            "      ],\n" +
            "      \"distanceMeters\": 2500,\n" +
            "      \"driverId\": 719,\n" +
            "      \"endAddress\": {\n" +
            "        \"address\": \"123 Main St, Sunnyvale, CA 94089\",\n" +
            "        \"id\": 581,\n" +
            "        \"name\": \"Ramen Tatsunoya\"\n" +
            "      },\n" +
            "      \"endCoordinates\": {\n" +
            "        \"latitude\": 23.413702345,\n" +
            "        \"longitude\": -91.502888123\n" +
            "      },\n" +
            "      \"endLocation\": \"571 S Lake Ave, Pasadena, CA 91101\",\n" +
            "      \"endMs\": 1462881998034,\n" +
            "      \"endOdometer\": 210430500,\n" +
            "      \"fuelConsumedMl\": 75700,\n" +
            "      \"startAddress\": {\n" +
            "        \"address\": \"123 Main St, Sunnyvale, CA 94089\",\n" +
            "        \"id\": 581,\n" +
            "        \"name\": \"Ramen Tatsunoya\"\n" +
            "      },\n" +
            "      \"startCoordinates\": {\n" +
            "        \"latitude\": 29.443702345,\n" +
            "        \"longitude\": -98.502888123\n" +
            "      },\n" +
            "      \"startLocation\": \"16 N Fair Oaks Ave, Pasadena, CA 91103\",\n" +
            "      \"startMs\": 1462878398034,\n" +
            "      \"startOdometer\": 210430450,\n" +
            "      \"tollMeters\": 32000\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SamsaraService samsaraService;
    @MockBean
    private SamsaraClient samsaraClient;
    @MockBean
    private FileDownloadClient fileDownloadClient;


    @Test
    public void jsonToVehiclesListPojo() throws JsonProcessingException {
        List<Double> ints = List.of(867D, 5309D);
        VehiclesList list = objectMapper.readValue(vehiclesJson, VehiclesList.class);
        assertNotNull(list.getData());
        assertArrayEquals(list.getData()
                .get(0)
                .getAttributes()
                .get(0)
                .getNumberValues()
                .toArray(), ints.toArray());
    }

    @Test
    public void jsonToTachographResponsePojo() throws JsonProcessingException {
        TachographList tachographList = objectMapper.readValue(tachographResponseJson, TachographList.class);
        assertNotNull(tachographList);
        assertEquals(tachographList.getData()
                .get(0)
                .getVehicle()
                .getExternalIds()
                .get("maintenanceId"), "250020");
    }

    @Test
    public void writeTachographFile() {
        List<TachographResponseDto> testResp = new ArrayList<>();
        TachographResponseDto dto = new TachographResponseDto();
        FileInfo fileInfo = new FileInfo();
        fileInfo.setId("Id1");
        fileInfo.setCreatedAtTime(String.valueOf(LocalDateTime.now()));
        fileInfo.setVehicleIdentificationNumber("VIN090909");
        dto.setFiles(List.of(fileInfo));
        dto.setVehicle(new VehicleTachographDto());
        testResp.add(dto);
        Path file = Path.of("./test.txt");
        when(fileDownloadClient.loadVideoFile(Mockito.any())).thenReturn(file);
        samsaraService.loadVideoTachographFiles(testResp);

    }
//
//    @Test
//    public void readEnvFromFileEnvTxt() throws IOException, ListIdsEmptyException {
//        TripList list = objectMapper.readValue(tripsJson, TripList.class);
//        when(samsaraService.loadAllVehicleTrips()).thenReturn(list.getTrips());
//        samsaraService.loadAllVehicleTrips();
//    }
//
//    @Test
//    public void jsonToFile() throws Exception {
//        VehiclesList list = objectMapper.readValue(vehiclesJson, VehiclesList.class);
//        when(samsaraService.getAllVehicles()).thenReturn(list.getData());
//        samsaraService.loadAllVehicles();
//    }
}