package com.fitness.controller;

import com.fitness.model.Recommendation;
import com.fitness.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")

@RequiredArgsConstructor
public class RecommendationController
{
    private final RecommendationService recommendationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getUserRecommendations(@PathVariable String userId)
    {
        return ResponseEntity.ok(recommendationService.getUserRecommendations(userId));
    }

    @GetMapping("user/activity/{activityId}")
    public ResponseEntity<Recommendation> getRecommendationForActivity(@PathVariable String activityId)
    {
        return ResponseEntity.ok(recommendationService.getRecommendationForActivity(activityId));
    }
}