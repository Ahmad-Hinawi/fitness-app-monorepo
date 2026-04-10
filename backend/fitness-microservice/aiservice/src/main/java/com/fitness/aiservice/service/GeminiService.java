package com.fitness.aiservice.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class GeminiService {
    private final WebClient webClient;
    private final RecommendationService recommendationService;
    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    public String getMessageOfActivity(Activity activity) {

        return String.format("""
                                Return the response ONLY as a valid JSON object. Do not include markdown formatting, backticks, or any preamble.
                                        Follow this exact JSON schema:
                        {
                            "analysis": {
                                   "overall": "string",
                                    "pace": "string",
                                    "caloriesBurned": "string",
                                    "heartRate": "string"
                        },
                            "improvements": [
                            {
                                "area": "string",
                                    "recommendations": "string"
                            }
                        ],
                            "suggestions": [
                            {
                                "workout": "string",
                                    "description": "string"
                            }
                        ],
                            "safety": ["string", "string"]
                        }
                        
                       User Profile Context:
            - Age: %d years old
            - Gender: %s
            - Weight: %.2f kg
            - Height: %.2f cm
            - Fitness Goal: %s
            
            Activity Data to Analyze:
            - Activity Type: %s
            - Duration: %d minutes
            - Calories Burned: %d
            - Additional Metrics: %s
            
            Instructions:
            Provide a detailed performance analysis based on the user's physical stats and fitness goal. 
            Suggest specific next steps to help them reach their %s goal.
            """,
                activity.getAge(),
                activity.getGender(),
                activity.getWeight(),
                activity.getHeight(),
                activity.getFitnessGoal(),
                activity.getActivityType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics(),
                activity.getFitnessGoal()
        );


    }

    public String askGemini(String question) {

       Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", question))))
        );

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemini-2.5-flash:generateContent")
                        .queryParam("key",apiKey)
                        .build())
         // API Key usually goes in URL
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody -> {
                            System.err.println("Gemini API Error: " + errorBody);
                            return Mono.error(new RuntimeException("API Error: " + errorBody));
                        })
                )
                .bodyToMono(String.class)
                .retryWhen(
                        reactor.util.retry.Retry.fixedDelay(3, java.time.Duration.ofSeconds(2))
                )
                .block();



    }

    public Recommendation convertResponsetoRecommendation(Activity activity) {System.out.println("RECEIVED ACTIVITY: " + activity);
       String prompt=getMessageOfActivity(activity);
       String response=askGemini(prompt);
       ObjectMapper mapper = new ObjectMapper();
       try {
           String cleanRaw=response.replaceAll("'''json|'''","").trim();
           JsonNode jsonroot = mapper.readTree(response);

           String innerJsonText =jsonroot
                   .path("candidates").get(0)
                   .path("content")
                   .path("parts").get(0)
                   .path("text").asText();
           JsonNode root=mapper.readTree(innerJsonText);
           Recommendation rec=new Recommendation();
           rec.setActivityId(activity.getActivityId() );
           rec.setActivityType(activity.getActivityType());
           rec.setUserId(activity.getUserId());
           List<String> improvements=new ArrayList<>();
           root.path("improvements").forEach(improvement -> {
               String area=improvement.get("area").asText();
               String recommendation=improvement.get("recommendations").asText();
               improvements.add(String.format("%s: %s",area,recommendation));
           });
           rec.setImprovements(improvements);
           List<String> suggestions=new ArrayList<>();
           root.path("suggestions").forEach(suggestion -> {
               String workoutName=suggestion.get("workout").asText();
               String description=suggestion.get("description").asText();
               suggestions.add(String.format("%s: %s",workoutName,description));
           });
           rec.setSuggestions(suggestions);
           List<String> safety=new ArrayList<>();
           root.path("safety").forEach(s->safety.add(s.asText()));
           rec.setSafety(safety);
           rec.setCreatedAt(LocalDateTime.now());
           StringBuilder analysis=new StringBuilder();
           JsonNode js=root.path("analysis");
           analysis.append("OverAll : ").append(js.path("overall").asText()).append("\n\n");
           analysis.append("Pace : ").append(js.path("pace").asText()).append("\n\n");
           analysis.append("Calories : ").append(js.path("caloriesBurned").asText()).append("\n\n");
           analysis.append("HeartRate : ").append(js.path("heartRate").asText()).append("\n\n");
           rec.setRecommendation(analysis.toString());
           return recommendationService.save(rec);

       }
       catch(Exception e){
           throw new RuntimeException("Failed to map AI response to Recommendation class", e);
       }
    }
}