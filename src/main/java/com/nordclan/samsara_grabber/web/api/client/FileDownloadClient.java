package com.nordclan.samsara_grabber.web.api.client;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.nio.file.Path;

@Data
@Component
@Slf4j
public class FileDownloadClient {

    public Path loadVideoFile(String source) {
        Path ret = Path.of("c:/temp", "tmp");
        try {
            URL url = new URL(source);
            FileUtils.copyURLToFile(url, ret.toFile());
        } catch (Exception e) {
            log.error("Error when download video - " + e.getMessage());
        }
        return ret;
    }
}
