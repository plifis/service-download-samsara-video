package com.nordclan.samsara_grabber.domain.tachograph;

import com.nordclan.samsara_grabber.domain.page.SamsaraPageImpl;
import lombok.Data;

import java.util.List;

@Data
public class TachographList {
    private List<TachographResponseDto> data;
    private SamsaraPageImpl pagination;
}
