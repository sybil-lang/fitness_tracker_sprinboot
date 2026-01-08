package com.fitness_monolith.fitness.controller;
import com.fitness_monolith.fitness.dto.ActivityRequest;
import com.fitness_monolith.fitness.dto.ActivityResponse;
import com.fitness_monolith.fitness.model.Activity;
import com.fitness_monolith.fitness.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor

public class ActivityController {


    private final ActivityService activityService;


    /*
       POST /api/activities
      - Track (create) a new activity
     */
    @PostMapping
    public ResponseEntity<ActivityResponse> createActivity(
            @RequestBody ActivityRequest request) {

        ActivityResponse response = activityService.createActivity(request);
        return ResponseEntity.ok(response);
    }

//    /*
//      GET /api/activities
//     - Retrieve all activities
//    */
//    @GetMapping
//    public List<Activity> getAllActivities() {
//        return activityService.getAllActivities();
//    }
}
