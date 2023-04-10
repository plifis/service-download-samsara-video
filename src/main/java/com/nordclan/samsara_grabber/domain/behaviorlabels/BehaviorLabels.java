package com.nordclan.samsara_grabber.domain.behaviorlabels;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class BehaviorLabels {
    private String label;
    private String name;
    private Source source;

    @AllArgsConstructor
    public enum Source {
        AUTOMATED("automated"),
        USER_GENERATED("userGenerated");

        private String value;

        @JsonValue
        public String getValue() {
            return value;
        }
    }
}
