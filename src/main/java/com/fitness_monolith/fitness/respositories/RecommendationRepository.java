package com.fitness_monolith.fitness.respositories;
import com.fitness_monolith.fitness.model.Activity;
import com.fitness_monolith.fitness.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, String> {

    List<Recommendation> findByUserId(String userId);

    List<Recommendation> findByActivityId(String activityId);
}