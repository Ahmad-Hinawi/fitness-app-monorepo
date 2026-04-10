package com.fitness.gateway_service.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestUser {
    @NotBlank
    private String keycloakId;
    @NotBlank
    @Size(min = 8,message = "password must be 8 Characters at least")
    private String password;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
