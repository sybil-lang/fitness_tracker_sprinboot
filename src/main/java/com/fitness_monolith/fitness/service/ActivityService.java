package com.fitness_monolith.fitness.service;

import com.fitness_monolith.fitness.dto.ActivityRequest;
import com.fitness_monolith.fitness.dto.ActivityResponse;
import com.fitness_monolith.fitness.model.Activity;
import com.fitness_monolith.fitness.model.ActivityType;
import com.fitness_monolith.fitness.model.User;
import com.fitness_monolith.fitness.respositories.ActivityRepository;
import com.fitness_monolith.fitness.respositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;


    /*
    📌 Entity → Response DTO
   */
    private ActivityResponse mapToResponse(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setType(ActivityType.valueOf(activity.getType().name()));
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        response.setUserId(activity.getUser().getId());
        return response;
    }

    /*
      Save a new activity
    */
    public ActivityResponse createActivity(ActivityRequest request) {
        // 1. Fetch user (owning side of relationship)
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Invalid user"));

        //  2. Build Activity entity using Builder
        Activity activity = Activity.builder()
//                .type(ActivityType.valueOf(String.valueOf(request.getType())))
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .user(user)   // 🔑 owning side sets user
                .build();

        //  3. Save activity
        Activity savedActivity = activityRepository.save(activity);

        //  4. Convert entity → response DTO
        return mapToResponse(savedActivity);
    }


    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity>activityList=activityRepository.findByUserId(userId);
        return activityList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }
}
