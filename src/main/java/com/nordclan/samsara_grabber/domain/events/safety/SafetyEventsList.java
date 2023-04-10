package com.nordclan.samsara_grabber.domain.events.safety;

import com.nordclan.samsara_grabber.domain.page.SamsaraPageImpl;
import lombok.Data;

import java.util.List;

@Data
public class SafetyEventsList {
    private List<SafetyEventsDto> data;
    private SamsaraPageImpl pagination;
}
