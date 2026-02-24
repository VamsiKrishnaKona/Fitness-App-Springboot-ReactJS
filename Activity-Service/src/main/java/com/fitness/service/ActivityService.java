package com.fitness.service;

import com.fitness.repository.ActivityRepository;
import com.fitness.DTOs.ActivityMapper;
import com.fitness.DTOs.ActivityRequest;
import com.fitness.DTOs.ActivityResponse;
import com.fitness.model.Activity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService
{
    private final ActivityRepository activityRepository;

    private final ActivityMapper activityMapper;

    private final UserValidationService userValidationWebClient;

    public ActivityResponse createActivity(ActivityRequest activityRequest)
    {
        boolean isValidUser = userValidationWebClient.validateUser(activityRequest.getUserId());

        if(!isValidUser)
        {
            throw new RuntimeException("Invalid user id");
        }

        Activity activity = mapRequestToActivity(activityRequest);
        Activity savedActivity = activityRepository.save(activity);
        return activityMapper.ActivityToActivityResponse(savedActivity);
    }

    public List<ActivityResponse> getActivityByUserId(String userId)
    {
        List<Activity> activityList = activityRepository.findByUserId(userId);

        return activityList.stream().map(this::mapActivitytoActivityResponse).collect(Collectors.toList());
    }

    /*----------------------------------------Helpers--------------------------------------------------*/

    public ActivityResponse mapActivitytoActivityResponse(Activity activity)
    {
        return activityMapper.ActivityToActivityResponse(activity);
    }

    public Activity mapRequestToActivity(ActivityRequest activityRequest)
    {
        return activityMapper.requestToActivity(activityRequest);
    }
}
