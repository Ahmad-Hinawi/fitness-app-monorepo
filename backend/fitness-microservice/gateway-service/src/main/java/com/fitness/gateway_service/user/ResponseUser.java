package com.fitness.gateway_service.user;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseUser {
    private String id;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
