package com.nordclan.samsara_grabber.domain.vehicle;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.nordclan.samsara_grabber.domain.gateway.GateWay;
import com.nordclan.samsara_grabber.domain.sensor.configuration.SensorConfiguration;
import com.nordclan.samsara_grabber.domain.assigneddriver.StaticAssignedDriver;
import com.nordclan.samsara_grabber.domain.vehicle.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class VehiclesDto {
    private List<Vehicle> attributes = new ArrayList<>();
    private String auxInputType1;
    private String auxInputType2;
    private String auxInputType3;
    private String auxInputType4;
    private String auxInputType5;
    private String auxInputType6;
    private String auxInputType7;
    private String auxInputType8;
    private String auxInputType9;
    private String auxInputType10;
    private String auxInputType11;
    private String auxInputType12;
    private String auxInputType13;
    private String cameraSerial;
    private LocalDateTime createdAtTime;
    private String esn;
    private Map<String, String> ExternalIds;

    @JsonProperty("gateway")
    private GateWay gateWay;
    private String harshAccelerationSettingType;
    private String id;
    private String licensePlate;
    private String make;
    private String model;
    private String name;
    private String notes;
    private SensorConfiguration sensorConfiguration;
    private String serial;
    private StaticAssignedDriver staticAssignedDriver;
    private List<Tag> tags;
    private LocalDateTime updatedAtTime;
    private VehicleRegulationMode vehicleRegulationMode;
    private String vin;
    private String year;

    @AllArgsConstructor
    public enum VehicleRegulationMode {
        REGULATED("regulated"),
        UNREGULATED("unregulated");

        private String value;

        @JsonValue
        public String getValue() {
            return value;
        }
    }

}
