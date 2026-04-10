package com.fitness.usersrvice.service;

import com.fitness.usersrvice.dao.RequestUser;
import com.fitness.usersrvice.dao.ResponseUser;
import com.fitness.usersrvice.model.User;
import com.fitness.usersrvice.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private ResponseUser ConvertUserToResponseUser(User user) {
        ResponseUser responseUser = new ResponseUser();
        responseUser.setKeycloakId(user.getKeycloakId());
        responseUser.setFirstName(user.getFirstName());
        responseUser.setLastName(user.getLastName());
        responseUser.setEmail(user.getEmail());
        responseUser.setRole(user.getRole());
        responseUser.setBirthDate(user.getBirthDate());
        responseUser.setGender(user.getGender());
        responseUser.setHeight(user.getHeight());
        responseUser.setWeight(user.getWeight());
        responseUser.setFitnessGoal(user.getFitnessGoal());
        return responseUser;
    }
    public ResponseUser getUser(String userId)  {
        User user;
        user = userRepository.findByKeycloakId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"
                ));
       ResponseUser responseUser = ConvertUserToResponseUser(user);
       System.out.println("responseUser--> "+responseUser.toString());
        return responseUser;
    }

   public boolean registerNewUser(@Valid RequestUser newUser) {
        // 1. Check if ID already exists
       if(existByUserId(newUser.getKeycloakId())) {
           return false;
       }

       System.out.println("User NOT found. Saving new user...");
       User user = new User();
       user.setKeycloakId(newUser.getKeycloakId());
       user.setFirstName(newUser.getFirstName());
       user.setLastName(newUser.getLastName());
       user.setEmail(newUser.getEmail());
       user.setRole("USER");


       userRepository.save(user);
       System.out.println("Save command executed.");

       return true;
    }
    public Boolean existByUserId(String userId) {
        return userRepository.existsById(userId);
    }
    public ResponseUser updateUserProfile(@Valid RequestUser updateUser) {
        User user = userRepository.findById(updateUser.getKeycloakId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"
                ));
        if (updateUser.getHeight() != null) {
            user.setHeight(updateUser.getHeight());
        } else {
            user.setHeight(0.0); // Or leave it null if ResponseUser uses Double
        }
        if (updateUser.getWeight() != null) {
            user.setWeight(updateUser.getWeight());
        } else {
            user.setWeight(0.0); // Or leave it null if ResponseUser uses Double
        }

        user.setBirthDate(updateUser.getBirthDate());
        user.setGender(updateUser.getGender());
        user.setFitnessGoal(updateUser.getFitnessGoal());
        userRepository.save(user);
        return ConvertUserToResponseUser(user);

    }
}
