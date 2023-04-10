package com.nordclan.samsara_grabber.domain.page;

import lombok.Data;

@Data
public class SamsaraPageImpl implements SamsaraPage {
    private String endCursor;
    private Boolean hasNextPage;
}
