package com.fitness.activityservice.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile {
    private String keycloakId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String gender;
    private String fitnessGoal;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

    private LocalDate birthDate;
    private Double weight;
    private Double height;

}
