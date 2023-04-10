package com.nordclan.samsara_grabber.domain.vehicle;

import com.nordclan.samsara_grabber.domain.page.SamsaraPageImpl;
import lombok.Data;

import java.util.List;

@Data
public class VehiclesList {
    private List<VehiclesDto> data;
    private SamsaraPageImpl pagination;

}