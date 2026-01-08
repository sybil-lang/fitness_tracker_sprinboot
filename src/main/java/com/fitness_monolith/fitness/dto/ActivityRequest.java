package com.fitness_monolith.fitness.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fitness_monolith.fitness.model.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ActivityRequest {
    private ActivityType type;
    private Integer duration;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String, Object> additionalMetrics;
    private String userId;
}
