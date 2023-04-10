package com.nordclan.samsara_grabber.domain.door;

import com.fasterxml.jackson.annotation.JsonValue;
import com.nordclan.samsara_grabber.domain.sensor.Sensor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Door {
    private Position position;
    private Sensor sensor;

    @AllArgsConstructor
    public enum Position {
        BACK("back"),
        LEFT("left"),
        RIGHT("right");

        private String value;

        @JsonValue
        public String getValue() {
            return value;
        }
    }
}
