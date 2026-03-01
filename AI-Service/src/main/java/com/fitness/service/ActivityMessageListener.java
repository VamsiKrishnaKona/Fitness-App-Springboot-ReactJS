package com.fitness.service;

import com.fitness.model.Activity;
import com.fitness.model.Recommendation;
import com.fitness.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class

ActivityMessageListener
{
    private final ActivityAiService activityAiService;
    private final RecommendationRepository recommendationRepository;

    @RabbitListener(queues = "activity.queue")
    private void processActivity(Activity activity)
    {
        log.info("Received Activity for processing: {}", activity.getId());
        //log.info("Generated Recommendation: {}", activityAiService.generateRecommendation(activity));

        Recommendation recommendation = activityAiService.generateRecommendation(activity);
        recommendationRepository.save(recommendation);
    }
}
