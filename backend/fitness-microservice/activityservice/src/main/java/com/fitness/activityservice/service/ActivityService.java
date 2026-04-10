package com.fitness.activityservice.service;

import com.fitness.activityservice.dao.ActivityFullContext;
import com.fitness.activityservice.dao.ActivityRequest;
import com.fitness.activityservice.dao.ActivityResponse;
import com.fitness.activityservice.dao.UserProfile;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private UserValidationService userValidationService;
    @Autowired
    private ActivityProducer activityProducer;


    public ActivityResponse trackActivity(ActivityRequest activityRequest) {

        UserProfile userProfile=userValidationService.validationUser(activityRequest.getUserId());
        System.out.println("userProfile:"+userProfile);
        int userAge = 0;
        if (userProfile.getBirthDate() != null) {
            userAge = Period.between(userProfile.getBirthDate(), LocalDate.now()).getYears();
        }
        Activity activity=Activity.builder()
                .userId(activityRequest.getUserId())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .duration(activityRequest.getDuration())
                .activityType(activityRequest.getActivityType())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .build();
         Activity savedActivity=activityRepository.save(activity);
         System.out.println("DEBUG: Generated savedActivity -> " + savedActivity.toString());

        ActivityFullContext context= ActivityFullContext.builder()
                 .id(savedActivity.getId())
                 .userId(activityRequest.getUserId())
                 .activityType(savedActivity.getActivityType())
                 .caloriesBurned(savedActivity.getCaloriesBurned())
                 .duration(savedActivity.getDuration())

                 .weight(userProfile.getWeight())
                 .height(userProfile.getHeight())
                 .gender(userProfile.getGender())
                 .age(userAge)
                .fitnessGoal(userProfile.getFitnessGoal())
                 .additionalMetrics(activityRequest.getAdditionalMetrics())
                 .build();
        System.out.println("DEBUG: Generated ActivityFullContext -> " + context.toString());

         ActivityResponse response=maptoActivityResponse(savedActivity);

         activityProducer.sendMessage(context);

        return response;

    }
    private ActivityResponse maptoActivityResponse(Activity activity) {
        ActivityResponse activityResponse=new ActivityResponse();
        activityResponse.setId(activity.getId());
        activityResponse.setUserId(activity.getUserId());
        activityResponse.setCaloriesBurned(activity.getCaloriesBurned());
        activityResponse.setDuration(activity.getDuration());
        activityResponse.setCreatedAt(activity.getCreatedAt());
        activityResponse.setActivityType(activity.getActivityType());
        activityResponse.setAdditionalMetrics(activity.getAdditionalMetrics());
        return activityResponse;

    }

    public List<ActivityResponse> getUserActivities(String userId) {
         List<Activity> list=activityRepository.findByUserId(userId);
         return list.stream().map(activity->maptoActivityResponse(activity))
                 .collect(Collectors.toList());


    }
}
