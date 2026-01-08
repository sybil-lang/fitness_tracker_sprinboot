package com.fitness_monolith.fitness.service;

import com.fitness_monolith.fitness.dto.RecommendationRequest;
import com.fitness_monolith.fitness.model.Activity;
import com.fitness_monolith.fitness.model.Recommendation;
import com.fitness_monolith.fitness.model.User;
import com.fitness_monolith.fitness.respositories.ActivityRepository;
import com.fitness_monolith.fitness.respositories.RecommendationRepository;
import com.fitness_monolith.fitness.respositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRepository userRepository;
    private  final ActivityRepository activityRepository;
    private final RecommendationRepository recommendationRepository;

    public Recommendation generateRecommendation(RecommendationRequest request) {

        User user=userRepository.findById(request.getUserId())
                .orElseThrow(()->new RuntimeException("user not found"+request.getUserId()));


        Activity activity=activityRepository.findById(request.getActivityId())
                .orElseThrow(()->new RuntimeException("activity not found"+request.getActivityId()));


        Recommendation recommendation=Recommendation.builder()
                .user(user)
                .activity(activity)
                .improvements(request.getImprovements())
                .suggestions(request.getSuggestions())
                .safety(request.getSafety())
                .build();

        return recommendationRepository.save(recommendation);
    }

    public List<Recommendation> getUserRecommendation(String userId) {
        return recommendationRepository.findByUserId(userId);
    }

    public List<Recommendation> getActivityRecommendation(String activityId) {
        return recommendationRepository.findByActivityId(activityId);
    }

}
