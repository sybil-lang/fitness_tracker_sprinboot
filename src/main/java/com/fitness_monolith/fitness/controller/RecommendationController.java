package com.fitness_monolith.fitness.controller;
import com.fitness_monolith.fitness.dto.RecommendationRequest;
import com.fitness_monolith.fitness.model.Recommendation;
import com.fitness_monolith.fitness.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor

public class RecommendationController {

    private final RecommendationService recommendationService;

    //generate recommendation -"/api/recommendation/generate"
    @PostMapping("/generate")
    public ResponseEntity<Recommendation> generateRecommendation(
            @RequestBody RecommendationRequest request
    ){
        Recommendation recommendation=recommendationService.generateRecommendation(request);
        return ResponseEntity.ok(recommendation);
    }



    //get user recommendation /api/recommendation/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>>getUserRecommendation(@PathVariable String userId){ 
        List<Recommendation>recommendationList=
                recommendationService.getUserRecommendation(userId);
        return ResponseEntity.ok(recommendationList);
    }


    //get activity recommendation /api/recommendation/activity/{activityId}
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<Recommendation>>getActivityRecommendation(@PathVariable String activityId){
        List<Recommendation> recommendationList=
                recommendationService.getActivityRecommendation(activityId);
        return ResponseEntity.ok(recommendationList);
    }
}
