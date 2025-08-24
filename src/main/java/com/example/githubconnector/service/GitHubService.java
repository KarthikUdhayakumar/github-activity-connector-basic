package com.example.githubconnector.service;

import com.example.githubconnector.dto.CommitDto;
import com.example.githubconnector.dto.RepoActivityDto;
import com.example.githubconnector.dto.RepoDto;
import com.example.githubconnector.exception.GithubConnectorException;
import com.example.githubconnector.model.OwnerType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GitHubService {

    private static final String API = "https://api.github.com";
    private final RestTemplate rt;

    public GitHubService(RestTemplate rt) {
        this.rt = rt;
    }

    /**
     * Returns whether the given owner is of type user or organization
     * @param owner name of the owner
     * @param hint type of the owner
     * @return owner type if hint is AUTO else returns hint itself
     */
    public OwnerType resolveOwnerType(String owner, OwnerType hint) {
        if (hint != null && hint != OwnerType.AUTO) return hint;
        Map<String, Object> node = rt.getForObject(
                API + "/users/{owner}", Map.class, owner);
        if (node == null) return OwnerType.USER;
        Object t = node.get("type");
        if (t != null && "Organization".equals(String.valueOf(t))) {
            return OwnerType.ORGANIZATION;
        }
        return OwnerType.USER;
    }

    public List<RepoDto> listRepos(String owner, OwnerType hint,
                                   int maxRepos) {
        try {
            OwnerType t = resolveOwnerType(owner, hint);
            String path = (t == OwnerType.ORGANIZATION)
                    ? "/orgs/{owner}/repos"
                    : "/users/{owner}/repos";

            int perPage = 100;
            int page = 1;
            List<RepoDto> repos = new ArrayList<>();

            while (repos.size() < maxRepos) {
                String url = API + path
                        + "?per_page=" + perPage + "&page=" + page
                        + "&sort=updated&direction=desc";
                ResponseEntity<List<Map<String, Object>>> resp =
                        rt.exchange(url, HttpMethod.GET, null,
                                new ParameterizedTypeReference<>() {
                                }, owner);

                List<Map<String, Object>> body = resp.getBody();
                if (body == null || body.isEmpty()) break;

                for (Map<String, Object> r : body) {
                    repos.add(RepoDto.toRepoDto(r));
                    if (repos.size() >= maxRepos) break;
                }
                page++;
            }
            return repos;
        } catch (HttpClientErrorException.TooManyRequests ex) {
            throw new GithubConnectorException(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage());
        } catch (HttpClientErrorException ex) {
            throw new GithubConnectorException(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getMessage());
        }
    }

    public List<CommitDto> listCommits(String owner, String repo,
                                       int perRepoCommits) {
        try {
            List<CommitDto> commitList = new ArrayList<>();
            int page = 1;
            while(commitList.size() < perRepoCommits){
                String url = API + "/repos/{owner}/{repo}/commits"
                        + "?per_page=" + (perRepoCommits-commitList.size()) + "&page="+page;
                ResponseEntity<List<Map<String,Object>>> resp =
                        rt.exchange(url, HttpMethod.GET, null,
                                new ParameterizedTypeReference<>() {}, owner, repo);
                List<Map<String,Object>> body = resp.getBody();
                if (body == null || body.isEmpty()) break;
                for(Map<String, Object> record: body){
                    commitList.add(
                            CommitDto.toCommitDto(record)
                    );
                    if (commitList.size() >= perRepoCommits) break;
                }
                page ++;
            }
            return commitList;
        } catch (HttpClientErrorException.Conflict e) {
            return List.of();
        } catch (HttpClientErrorException.TooManyRequests ex) {
            throw new GithubConnectorException(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage());
        } catch (HttpClientErrorException ex) {
            throw new GithubConnectorException(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getMessage());
        }
    }

    public List<RepoActivityDto> fetchRepoActivities(
            String owner,
            int perRepoCommits,
            int maxRepos,
            OwnerType ownerType) {

        List<RepoDto> repos = listRepos(owner, ownerType, maxRepos);
        List<RepoActivityDto> result = new ArrayList<>();
        for (RepoDto r : repos) {
            String repoName = r.getName();
            List<CommitDto> commits =
                listCommits(owner, repoName, perRepoCommits);
            result.add(new RepoActivityDto(r, commits));
        }
        return result;
    }
}
