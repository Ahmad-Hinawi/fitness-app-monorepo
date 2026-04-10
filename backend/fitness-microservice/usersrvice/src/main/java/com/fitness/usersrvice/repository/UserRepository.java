package com.fitness.usersrvice.repository;

import com.fitness.usersrvice.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {


    Optional<User> findByEmail(@NotBlank @Email String email);
    Optional<User> findByKeycloakId(@NotBlank  String keycloakId);
}
