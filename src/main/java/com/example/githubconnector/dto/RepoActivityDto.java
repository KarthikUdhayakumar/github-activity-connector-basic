package com.example.githubconnector.dto;

import java.util.List;

public class RepoActivityDto {
    private RepoDto repo;
    private List<CommitDto> commits;

    public RepoActivityDto() {}

    public RepoActivityDto(RepoDto repo, List<CommitDto> commits) {
        this.repo = repo;
        this.commits = commits;
    }

    public RepoDto getRepo() { return repo; }
    public void setRepo(RepoDto repo) { this.repo = repo; }

    public List<CommitDto> getCommits() { return commits; }
    public void setCommits(List<CommitDto> commits) {
        this.commits = commits;
    }
}
