package com.fitness.aiservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection="recommendations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Recommendation {
    @Id
    private String id;
    private String activityId;
    private String userId;
    //@JsonProperty("type")
    private String activityType;
    private String recommendation;
    private List<String> improvements;
    private List<String> suggestions;
    private List<String> safety;
    @CreatedDate
    private LocalDateTime createdAt;

}
