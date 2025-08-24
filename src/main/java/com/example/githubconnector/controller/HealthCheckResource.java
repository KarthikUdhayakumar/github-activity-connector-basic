package com.example.githubconnector.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthCheckResource {
    @GetMapping("health")
    public ResponseEntity<Map<String, String>> healthCheck(){
        return ResponseEntity.ok(Map.of(
                "status", "Successful"
        ));
    }
}
