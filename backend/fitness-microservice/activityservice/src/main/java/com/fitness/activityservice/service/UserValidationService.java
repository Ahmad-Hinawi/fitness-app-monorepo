package com.fitness.activityservice.service;


import com.fitness.activityservice.dao.UserProfile;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class UserValidationService {
    private final WebClient userServiceWebClient;

    public UserProfile validationUser(String userId) {
        UserProfile userProfile = new UserProfile();

        try {
            userProfile = userServiceWebClient.get()
                    .uri("/api/users/"+userId) // Service name from Eureka

                    .retrieve()
                    .bodyToMono(UserProfile.class)
                    .block();

            if (userProfile == null) {
                throw new RuntimeException("User service returned an empty body");
            }



            // Handle null response safely


        } catch (Exception e) {
            System.out.println("ERROR OCCURRED:");
            e.printStackTrace();
            throw new RuntimeException("User service call failed", e);
        }
        return userProfile;
    }


}
