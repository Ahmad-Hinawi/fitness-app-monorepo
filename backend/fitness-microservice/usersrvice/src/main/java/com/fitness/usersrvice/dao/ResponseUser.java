package com.fitness.usersrvice.dao;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ResponseUser {

    private String keycloakId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String gender;
    private String fitnessGoal;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private Double height;
    private Double weight;

}
