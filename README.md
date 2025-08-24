# GitHub Repository Activity Connector (Spring Boot + Gradle)

This service fetches public repositories for a GitHub user
or organization and, for each repository, returns the last
**N commits** with message, author and timestamp. It uses a
Personal Access Token (PAT) when provided to raise rate
limits and access private data (if the token has scopes).
It also exposes a REST endpoint and an auto-generated
Swagger UI.

---

## Quick Start (Step-by-step)

### 1) Prerequisites
- **Java 17+**
- **Gradle 8+** (or use the Gradle wrapper if you add it)
- A GitHub **Personal Access Token (optional but recommended)**

### 2) Clone/Unzip
Unzip this project locally. Folder name:
`github-activity-connector`

### 3) Configure Token (optional)
Export your token so the app can authenticate:
```bash
export GITHUB_TOKEN=ghp_your_token_here
```
*Without a token you can still run the service, but the
GitHub API has low anonymous rate limits.*

### 4) Build & Run
```bash
cd github-activity-connector-basic
gradle clean bootRun
```
The app starts on **http://localhost:8080**.

### 5) Try the API
Fetch the last **20 commits per repo** (default) for owner
`spring-projects` (auto-detect user/org):
```bash
curl "http://localhost:8080/api/v1/owners/spring-projects/\
repo-activities?commits=20&maxRepos=10"
```

### 6) Swagger / OpenAPI
Open **http://localhost:8080/swagger-ui** in your browser
to try the endpoint, or get raw spec at:
**http://localhost:8080/v1/api-docs**

---

> The live, machine-readable spec is served at:
> **/v1/api-docs** by `springdoc-openapi`.

---

## Design Notes

- **Owner detection**: we query `/users/{{owner}}` and check
  `"type": "User" | "Organization"`. You may override via
  `ownerType` query param.
- **Pagination**: repos and commits are paged with `per_page` and `page` query parameters.
- **Rate limits**: the service returns appropriate error, keeping the API
  responsive.
- **Auth**: add `GITHUB_TOKEN` env var to authenticate.
- **Extensibility**: service + DTO design is simple to extend
  with more GitHub fields if needed.

---

## CLI Usage (optional pattern)

You can easily add a `CommandLineRunner` to call the service
at startup and print JSON. Example snippet:

```java
// in Application.java
//@Bean
//CommandLineRunner cli(GitHubService svc) {
//  return args -> {
//    var data = svc.fetchRepoActivities("spring-projects",
//        5, 5, OwnerType.AUTO);
//    System.out.println(new ObjectMapper().writeValueAsString(data));
//  };
//}
```

Enable it (uncomment), then run `gradle bootRun`.

---
## Build a Jar
```bash
gradle clean bootJar
java -jar build/libs/github-activity-connector-0.0.1-SNAPSHOT.jar
```

---

## Troubleshooting

- **HTTP 403/429**: you're likely rate-limited. Set
  `GITHUB_TOKEN` and retry.
- **No repos returned**: ensure the owner handle is correct
  and public repos exist.
- **Swagger UI not loading**: check `springdoc` paths in
  `application.properties`.

---