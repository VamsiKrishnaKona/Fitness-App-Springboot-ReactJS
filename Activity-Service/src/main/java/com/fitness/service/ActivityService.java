package com.fitness.service;

import com.fitness.repository.ActivityRepository;
import com.fitness.DTOs.ActivityMapper;
import com.fitness.DTOs.ActivityRequest;
import com.fitness.DTOs.ActivityResponse;
import com.fitness.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService
{
    private final ActivityRepository activityRepository;

    private final ActivityMapper activityMapper;

    private final UserValidationService userValidationWebClient;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public ActivityResponse createActivity(ActivityRequest activityRequest)
    {
        boolean isValidUser = userValidationWebClient.validateUser(activityRequest.getUserId());

        if(!isValidUser)
        {
            throw new RuntimeException("Invalid user id");
        }

        Activity activity = mapRequestToActivity(activityRequest);
        Activity savedActivity = activityRepository.save(activity);

        try
        {
            rabbitTemplate.convertAndSend(exchange, routingKey, savedActivity);
        }
        catch(Exception e)
        {
            log.error("Failed to publish Activity to RabbitMQ : ", e);
        }

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
