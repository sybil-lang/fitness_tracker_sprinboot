package com.fitness_monolith.fitness.respositories;

import com.fitness_monolith.fitness.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, String> {
    List<Activity> findByUserId(String userId);
}