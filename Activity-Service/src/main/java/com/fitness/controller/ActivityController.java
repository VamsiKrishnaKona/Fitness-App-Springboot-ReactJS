package com.fitness.controller;

import com.fitness.DTOs.ActivityRequest;
import com.fitness.DTOs.ActivityResponse;
import com.fitness.model.Activity;
import com.fitness.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")

@AllArgsConstructor
public class ActivityController
{
    private ActivityService activityService;

    @PostMapping("/addActivity")
    public ResponseEntity<ActivityResponse> addActivity(@RequestBody ActivityRequest activityRequest)
    {
        return ResponseEntity.ok(activityService.createActivity(activityRequest));
    }
}
