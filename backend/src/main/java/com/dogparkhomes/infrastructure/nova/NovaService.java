package com.dogparkhomes.infrastructure.nova;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dogparkhomes.api.dto.SearchFiltersDto;

import java.nio.charset.StandardCharsets;

@Service
public class NovaService {

    private final BedrockRuntimeClient bedrockRuntimeClient;

    public NovaService(BedrockRuntimeClient bedrockRuntimeClient) {
        this.bedrockRuntimeClient = bedrockRuntimeClient;
    }

    public SearchFiltersDto parseUserQuery(String query) {

        try {
            String requestBody = """
            {
              "messages": [
                {
                  "role": "user",
                  "content": [
                    {
                      "text": "You are a real estate search assistant. Extract structured search filters from the user query. Return ONLY JSON. Do not explain. User query: %s"
                    }
                  ]
                }
              ],
              "inferenceConfig": {
                "maxTokens": 200,
                "temperature": 0.1
              }
            }
            """.formatted(query);

            InvokeModelRequest request = InvokeModelRequest.builder()
                    .modelId("us.amazon.nova-2-lite-v1:0")
                    .contentType("application/json")
                    .accept("application/json")
                    .body(SdkBytes.fromString(requestBody, StandardCharsets.UTF_8))
                    .build();

            InvokeModelResponse response = bedrockRuntimeClient.invokeModel(request);

            String raw = response.body().asUtf8String();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(raw);

            String text = root
                    .path("output")
                    .path("message")
                    .path("content")
                    .get(0)
                    .path("text")
                    .asText();

            text = text.replace("```json", "")
                       .replace("```", "")
                       .trim();

            ObjectMapper mapper2 = new ObjectMapper();
            SearchFiltersDto filters = mapper2.readValue(text, SearchFiltersDto.class);
            return filters;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error calling Nova", e);
        }
    }
}
