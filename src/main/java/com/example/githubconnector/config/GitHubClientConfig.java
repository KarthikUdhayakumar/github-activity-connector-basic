package com.example.githubconnector.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class GitHubClientConfig {

    @Value("${github.token:}")
    private String githubToken;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate rt = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors =
                new ArrayList<>();

        interceptors.add((request, body, execution) -> {
            request.getHeaders().add("User-Agent",
                    "github-activity-connector/0.0.1");
            if (githubToken != null && !githubToken.isBlank()) {
                request.getHeaders().add("Authorization",
                        "Bearer " + githubToken);
            }
            return execution.execute(request, body);
        });
        rt.setInterceptors(interceptors);
        return rt;
    }
}
