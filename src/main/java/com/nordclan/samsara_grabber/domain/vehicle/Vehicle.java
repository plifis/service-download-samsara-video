package com.nordclan.samsara_grabber.domain.vehicle;

import lombok.Data;

import java.util.List;

@Data
public class Vehicle {
    private String id;
    private String name;
    private List<Double> numberValues;
    private List<String> stringValues;

}
