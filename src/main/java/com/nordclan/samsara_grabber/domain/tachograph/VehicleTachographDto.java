package com.nordclan.samsara_grabber.domain.tachograph;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class VehicleTachographDto {
    @JsonProperty("ExternalIds")
    private Map<String, String> externalIds;
    private String id;
    private String name;
}
