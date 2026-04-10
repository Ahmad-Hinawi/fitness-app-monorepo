package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityMessageListener {
    private final GeminiService geminiService;

    @RabbitListener(queues="fitness.activityservice.rabbitmq")
    public void  activityProcess(Activity activity){
      log.info("RECEIVED ACTIVITY FOR AI PROCESSING: {}", activity);
      try{
          geminiService.convertResponsetoRecommendation(activity);
          log.info("Successfully generated recommendation for Activity ID: {}", activity.getActivityId());

      }
      catch(Exception e){
         // throw new RuntimeException("AI processing failed for activity " + activity.getActivityId(), e);
      log.error("CRITICAL: AI processing failed for activity {}. Error: {}",
                  activity.getActivityId(), e.getMessage());
      }
    }

}
