package com.nordclan.samsara_grabber.domain.trip;

import com.nordclan.samsara_grabber.domain.location.Location;
import com.nordclan.samsara_grabber.domain.address.Address;
import lombok.Data;

import java.util.List;

/**
 * Entity of response from "<a href="https://api.samsara.com/v1/fleet/trips">...</a>"
 */
@Data
public class Trip {
    private List<Integer> assetIds;
    private List<Integer> codriverIds;
    private Integer distanceMeters;
    private Integer driverId;
    private Address endAddress;
    private Location endCoordinates;
    private String endLocation;
    private Long endMs;
    private Integer endOdometer;
    private Integer fuelConsumedMl;
    private Address startAddress;
    private Location startCoordinates;
    private String startLocation;
    private Long startMs;
    private Integer startOdometer;
    private Integer tollMeters;
}
