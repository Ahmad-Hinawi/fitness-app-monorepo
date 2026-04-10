package com.fitness.activityservice.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;

// ... inside the class


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityFullContext {
    private String id;
    private String userId;


    // Physical Stats
    private Double weight;
    private Double height;
    private Integer age; // <--- The calculated field
    private String gender;
    private String fitnessGoal;

    // Activity Data

    private String activityType;
    private Integer duration;
    private Integer caloriesBurned;
    private Map<String,Object> additionalMetrics;

}
