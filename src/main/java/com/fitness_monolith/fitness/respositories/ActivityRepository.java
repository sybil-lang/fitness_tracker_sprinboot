package com.fitness_monolith.fitness.respositories;

import com.fitness_monolith.fitness.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, String> {
}