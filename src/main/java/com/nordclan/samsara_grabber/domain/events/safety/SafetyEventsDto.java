package com.nordclan.samsara_grabber.domain.events.safety;

import com.fasterxml.jackson.annotation.JsonValue;
import com.nordclan.samsara_grabber.domain.behaviorlabels.BehaviorLabels;
import com.nordclan.samsara_grabber.domain.location.Location;
import com.nordclan.samsara_grabber.domain.vehicle.VehiclesDto;
import com.nordclan.samsara_grabber.domain.assigneddriver.StaticAssignedDriver;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class SafetyEventsDto {
    private List<BehaviorLabels> behaviorLabels;
    private CoachingState coachingState;
    private String downloadForwardVideoUrl;
    private String downloadInwardVideoUrl;
    private String downloadTrackedInwardVideoUrl;
    private StaticAssignedDriver driver;
    private String id;
    private Location location;
    private Double maxAccelerationGForce;
    private String time;
    private VehiclesDto vehicle;


    @AllArgsConstructor
    public enum CoachingState {
        NEEDS_REVIEW("needsReview"),
        COACHED("coached"),
        DISMISSED("dismissed"),
        REVIEWED("reviewed"),
        ARCHIVED("archived"),
        MANUAL_REVIEW("manualReview"),
        NEEDS_COACHING("needsCoaching"),
        AUTO_DISMISSED("autoDismissed"),
        NEEDS_RECOGNITION("needsRecognition"),
        RECOGNIZED("recognized"),
        INVALID("invalid");


        private String value;

        @JsonValue
        public String getValue() {
            return value;
        }
    }
}
