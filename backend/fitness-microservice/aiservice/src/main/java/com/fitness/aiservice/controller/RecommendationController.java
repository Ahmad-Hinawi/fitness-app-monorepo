package com.fitness.aiservice.controller;

import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.service.GeminiService;
import com.fitness.aiservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class RecommendationController {
    private final RecommendationService recommendationService;
    private final GeminiService geminiService;
    private final WebClient webClient;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getUserRecommendations(@PathVariable String userId) {
        return ResponseEntity.ok(recommendationService.getUserRecommendation(userId));
    }
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recommendation> getActivityRecommendations(@PathVariable String activityId) {
        return ResponseEntity.ok(recommendationService.getActivityRecommendation(activityId));
    }
    @PostMapping("/activity")
    public ResponseEntity<Recommendation> createActivityRecommendation(@RequestBody Activity activity) {
       return  ResponseEntity.ok(geminiService.convertResponsetoRecommendation(activity));

    }
    @GetMapping("/test")
    public ResponseEntity<String> getTestRecommendations() {
        return ResponseEntity.ok("/test of ai-service");
    }



}
