package com.nordclan.samsara_grabber.domain.tachograph;

import com.nordclan.samsara_grabber.domain.fileinfo.FileInfo;
import lombok.Data;

import java.util.List;

@Data
public class TachographResponseDto {
    private List<FileInfo> files;
    private VehicleTachographDto vehicle;
}
