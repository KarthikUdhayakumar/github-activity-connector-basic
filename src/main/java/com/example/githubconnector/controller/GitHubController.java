package com.example.githubconnector.controller;

import com.example.githubconnector.dto.ErrorResponse;
import com.example.githubconnector.dto.RepoActivityDto;
import com.example.githubconnector.model.OwnerType;
import com.example.githubconnector.service.GitHubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated
public class GitHubController {

    private final GitHubService service;

    public GitHubController(GitHubService service) {
        this.service = service;
    }

    @GetMapping(value = "/owners/{owner}/repo-activities",
                produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Fetch repo activities last N (default 10) commits per repo)",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "OK",
                            content = @Content(array = @ArraySchema(
                                    schema = @Schema(implementation = RepoActivityDto.class)))),
                    @ApiResponse(responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "429",
                            description = "Rate Limited",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500",
                            description = "Server error",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public List<RepoActivityDto> getRepoActivities(
            @Parameter(description = "GitHub user/org handle")
            @PathVariable @NotBlank String owner,
            @Parameter(description = "Commits per repo (default 10)")
            @RequestParam(defaultValue = "20")
            @Min(1) int commits,
            @Parameter(description = "Max repositories (default 50)")
            @RequestParam(defaultValue = "50")
            @Min(1) @Max(1000) int maxRepos,
            @Parameter(description = "Owner type hint: USER, ORGANIZATION, "
                    + "AUTO (default AUTO)")
            @RequestParam(defaultValue = "AUTO")
            OwnerType ownerType) {

        return service.fetchRepoActivities(owner, commits,
                Math.min(maxRepos, 1000), ownerType);
    }
}
