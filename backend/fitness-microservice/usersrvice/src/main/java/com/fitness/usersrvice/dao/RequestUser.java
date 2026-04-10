package com.fitness.usersrvice.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestUser {
    private String keycloakId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String gender;
    private String fitnessGoal;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private Double height;
    private Double weight;
}
