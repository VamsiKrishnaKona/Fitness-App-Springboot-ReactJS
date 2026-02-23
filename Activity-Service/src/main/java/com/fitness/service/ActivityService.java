package com.fitness.service;

import com.fitness.ActivityRepository;
import com.fitness.DTOs.ActivityMapper;
import com.fitness.DTOs.ActivityRequest;
import com.fitness.DTOs.ActivityResponse;
import com.fitness.model.Activity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService
{
    private final ActivityRepository activityRepository;

    private final ActivityMapper activityMapper;

    public ActivityResponse createActivity(ActivityRequest activityRequest)
    {
        Activity activity = activityMapper.requestToActivity(activityRequest);
        Activity savedActivity = activityRepository.save(activity);
        return activityMapper.ActivityToActivityResponse(savedActivity);
    }
}
