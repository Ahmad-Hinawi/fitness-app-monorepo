package com.fitness.usersrvice.controller;

import com.fitness.usersrvice.dao.RequestUser;
import com.fitness.usersrvice.dao.ResponseUser;
import com.fitness.usersrvice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;



    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUser> getUserprofile(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/{userId}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable String userId) {
        return ResponseEntity.ok(userService.existByUserId(userId));
    }
    @Transactional
    @PostMapping("/sync")
    public ResponseEntity<String> syncUser(@Valid @RequestBody RequestUser newUser) {
        boolean isNew = userService.registerNewUser(newUser);
        if (isNew) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } else {
            return ResponseEntity.ok("User already exists, profile synced");
        }
    }
    @PutMapping("/{userId}")
    public ResponseEntity<ResponseUser> updateUser(@PathVariable String userId, @Valid @RequestBody RequestUser newUser) {
         return ResponseEntity.ok(userService.updateUserProfile(newUser));
    }
    @GetMapping("/test")
    public ResponseEntity<String> testUser() {
        return ResponseEntity.ok("this is test of user");
    }


}
