package com.nordclan.samsara_grabber.domain.area;

import com.fasterxml.jackson.annotation.JsonValue;
import com.nordclan.samsara_grabber.domain.sensor.Sensor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Area {
    List<Sensor> cargoSensors = new ArrayList<>();
    List<Sensor> humiditySensors = new ArrayList<>();
    private Position position;
    List<Sensor> temperatureSensors = new ArrayList<>();

    @AllArgsConstructor
    public enum Position {
        BACK("back"),
        FRONT("front"),
        MIDDLE("middle");

        private String value;

        @JsonValue
        public String getValue() {
            return value;
        }
    }
}
