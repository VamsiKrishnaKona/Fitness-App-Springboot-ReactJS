package com.fitness.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.model.Activity;
import com.fitness.model.Recommendation;
import com.fitness.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAiService
{
    private final GeminiService geminiService;

    private final RecommendationRepository recommendationRepository;


    public Recommendation generateRecommendation(Activity activity)
    {
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getAnswer(prompt);
        log.info("Response from AI: {}", aiResponse);
        return processAiResponse(activity, aiResponse);
    }

    private String createPromptForActivity(Activity activity)
    {
        return String.format("""
                Analyze this fitness activity and provide detailed recommendations in the following exact JSON format:
                {
                    "analysis": {
                        "overall": "Overall analysis here",
                        "pace": "Pace analysis here",
                        "heartRate": "Heart rate analysis here",
                        "caloriesBurned": "Calories analysis here"
                    },
                    "improvements": [
                        {
                            "area": "Area name",
                            "recommendation": "Detailed Recommendation"
                        }
                    ],
                    "suggestions": [
                        {
                            "workout": "Workout name",
                            "description": "Detailed workout description"
                        }
                    ],
                    "safety": [
                        "safety point 1",
                        "safety point 2"
                    ]
                }
                
                Analyze the activity:
                Activity Type: %s
                Duration: %d minutes
                Calories burned: %d
                Additional Metrics: %s
                
                Provide detailed analysis focusing on performance, improvements, next workout suggestions and safety guidelines.
                Ensure the response follows the EXACT JSON Format shown above.
                """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );
    }

    private Recommendation processAiResponse(Activity activity, String aiResponse)
    {
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);

            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    ;

            String jsonContent = textNode.asText()
                    .replaceAll("```json\\n", "")
                    .replaceAll("\\n```", "")
                    .trim();

            log.info("Parsed response from AI: {}", jsonContent);

            JsonNode analysisJSON = mapper.readTree(jsonContent);
            JsonNode analysisNode = analysisJSON.path("analysis");
            StringBuilder fullAnalysis = new StringBuilder();

            addAnalysisSection(fullAnalysis, analysisNode, "overall", "Overall:");
            addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace:");
            addAnalysisSection(fullAnalysis, analysisNode, "heartRate", "Heart Rate:");
            addAnalysisSection(fullAnalysis, analysisNode, "caloriesBurned", "Calories:");

            List<String> improvements = extractImprovements(analysisJSON.path("improvements"));
            List<String> suggestions = extractSuggestions(analysisJSON.path("suggestions"));
            List<String> safety = extractSafety(analysisJSON.path("safety"));

            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation(fullAnalysis.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .createdAt(LocalDateTime.now())
                    .build();
        }
        catch(Exception e)
        {
            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation("Unable to generate detailed analysis")
                    .improvements(Collections.singletonList("Continue with your current routine"))
                    .suggestions(Collections.singletonList("Consider consulting a fitness professional"))
                    .safety(Arrays.asList(
                            "Always warmup before exercises",
                            "Stay Hydrated",
                            "Have healthy diet",
                            "Listen to your body"
                    ))
                    .createdAt(LocalDateTime.now())
                    .build();
        }
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix)
    {
        if(!analysisNode.path(key).isMissingNode())
        {
            fullAnalysis.append(prefix)
                    .append(analysisNode.path(key).asText())
                    .append("\n\n");
        }
    }

    private List<String> extractImprovements(JsonNode improvementsNode)
    {
        List<String> improvements = new ArrayList<>();

        if(improvementsNode.isArray())
        {
            improvementsNode.forEach(improvement -> {
                String area = improvement.path("area").asText();
                String detail = improvement.path("recommendation").asText();

                improvements.add(String.format("%s: %s", area, detail));
            });
        }

        return improvements.isEmpty() ? Collections.singletonList("No specific improvements provided") :
                improvements;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode)
    {
        List<String> suggestions = new ArrayList<>();

        if(suggestionsNode.isArray())
        {
            suggestionsNode.forEach(suggestion -> {
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();

                suggestions.add(String.format("%s: %s", workout, description));
            });
        }

        return suggestions.isEmpty() ? Collections.singletonList("No specific suggestions provided") :
                suggestions;
    }

    private List<String> extractSafety(JsonNode safetyNode)
    {
        List<String> safety = new ArrayList<>();

        if(safetyNode.isArray())
        {
            safetyNode.forEach(item -> safety.add(item.asText()));
        }

        return safety.isEmpty() ? Collections.singletonList("Follow general guidelines.") : safety;
    }
}
