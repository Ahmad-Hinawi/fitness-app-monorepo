package com.fitness.activityservice.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ActivityRequest {
    private String id;
    private String userId;

    private String activityType;
    private Integer duration;
    private Integer caloriesBurned;
    private Map<String,Object> additionalMetrics;


}
