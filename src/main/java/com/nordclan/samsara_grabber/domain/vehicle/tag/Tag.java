package com.nordclan.samsara_grabber.domain.vehicle.tag;

import lombok.Data;

@Data
public class Tag {
    private String id;
    private String name;
    private String parentTagId;
}
