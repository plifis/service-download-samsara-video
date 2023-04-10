package com.nordclan.samsara_grabber.domain.sensor.configuration;

import com.nordclan.samsara_grabber.domain.door.Door;
import com.nordclan.samsara_grabber.domain.area.Area;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SensorConfiguration {
    List<Area> areas = new ArrayList<>();
    List<Door> doors = new ArrayList<>();
}
