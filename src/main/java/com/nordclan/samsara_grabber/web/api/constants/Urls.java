package com.nordclan.samsara_grabber.web.api.constants;

/**
 * Routes
 */
public interface Urls {

    String API_URL = "https://api.eu.samsara.com/";

    String VERSION = "v1";

    String FLEET = "fleet";

    interface Vehicles {
        String PART = "vehicles";
        String FULL = API_URL + FLEET + "/" + PART;

        interface Tachograph {
            String PART = "tachograph-files";
            String HISTORY = "history";
            String FULL = Vehicles.FULL + "/" + PART + "/" + HISTORY;
        }
    }

    interface Trips {
        String PART = "trips";
        String FULL = API_URL + VERSION + "/" + FLEET + "/" + PART;
    }

    interface SafetyEvents {
        String PART = "safety-events";
        String FULL = API_URL + FLEET + "/" +  PART;
    }


}
