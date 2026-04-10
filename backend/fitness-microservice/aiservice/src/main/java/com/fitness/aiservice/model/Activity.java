package com.fitness.aiservice.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


import java.time.LocalDateTime;
import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Activity {
    @JsonProperty("id")
    private String activityId;
    private String userId;

    // Physical Stats
    private Double weight;
    private Double height;
    private Integer age; // <--- The calculated field
    private String gender;
    private String fitnessGoal;

    // Activity Data
    //@JsonProperty("type")
    private String activityType;
    private Integer duration;
    private Integer caloriesBurned;
    private Map<String,Object> additionalMetrics;

}
