package com.nordclan.samsara_grabber.service;

import com.nordclan.samsara_grabber.exceptions.ListIdsEmptyException;

import java.io.IOException;
import java.util.List;

public interface ISamsaraService {

    void loadAllVehicles();

    void loadAllVehicleTrips() throws IOException, ListIdsEmptyException;

    void loadTachographVehicleFiles() throws IOException;

    <T> void writeResponse(List<T> response) throws IOException;

    void getAllSafetyEvents() throws IOException;
}
