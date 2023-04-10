package com.nordclan.samsara_grabber.config;


import com.nordclan.samsara_grabber.service.ISamsaraService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SamsaraCommandLineRunner implements CommandLineRunner {
    private final ISamsaraService iSamsaraService;

    @Override
    public void run(String... args) throws Exception {
        for (String arg : args) {
            if (arg.equals("-vs")) {
                iSamsaraService.loadAllVehicles();
            }
            if (arg.equals("-tr")) {
                iSamsaraService.loadAllVehicleTrips();
            }
            if (arg.equals("-vo")) {
                iSamsaraService.loadTachographVehicleFiles();
            }
            if (arg.equals("-se")) {
                iSamsaraService.getAllSafetyEvents();
            }
        }
    }

}
