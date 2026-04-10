package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationsRepository recommendationsRepository;

    public Recommendation save(Recommendation recommendation){
        return recommendationsRepository.save(recommendation);
    }
    public List<Recommendation> getUserRecommendation(String userId) {

        return recommendationsRepository.findByUserId(userId);


    }

    public Recommendation getActivityRecommendation(String activityId) {
        return recommendationsRepository.findByActivityId(activityId)
                .orElse(null);
    }
}
