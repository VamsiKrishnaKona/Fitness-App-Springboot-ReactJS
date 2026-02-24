package com.fitness.controller;

import com.fitness.DTOs.ActivityRequest;
import com.fitness.DTOs.ActivityResponse;
import com.fitness.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{userId}")
    public ResponseEntity<List<ActivityResponse>> getUserActivities(@PathVariable String userId)
    {
        return ResponseEntity.ok(activityService.getActivityByUserId(userId));
    }
}
