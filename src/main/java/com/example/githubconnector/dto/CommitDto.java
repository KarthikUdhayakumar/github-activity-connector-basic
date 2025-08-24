package com.example.githubconnector.dto;

import java.time.Instant;
import java.util.Map;

public class CommitDto {
    private String sha;
    private String message;
    private String authorName;
    private String authorEmail;
    private Instant date;
    private String htmlUrl;

    public CommitDto() {}

    public CommitDto(String sha, String message,
                     String authorName, String authorEmail,
                     Instant date, String htmlUrl) {
        this.sha = sha;
        this.message = message;
        this.authorName = authorName;
        this.authorEmail = authorEmail;
        this.date = date;
        this.htmlUrl = htmlUrl;
    }

    public String getSha() { return sha; }
    public void setSha(String sha) { this.sha = sha; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorEmail() { return authorEmail; }
    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public Instant getDate() { return date; }
    public void setDate(Instant date) { this.date = date; }

    public String getHtmlUrl() { return htmlUrl; }
    public void setHtmlUrl(String htmlUrl) { this.htmlUrl = htmlUrl; }

    public static CommitDto toCommitDto(Map<String,Object> n) {
        String sha = str(n.get("sha"));
        String html = str(n.get("html_url"));
        Map<String,Object> commit = cast(n.get("commit"));
        String msg = commit != null ? str(commit.get("message")) : null;

        String authorName = null, authorEmail = null;
        Instant date = null;
        if (commit != null) {
            Map<String,Object> author = cast(commit.get("author"));
            if (author != null) {
                authorName = str(author.get("name"));
                authorEmail = str(author.get("email"));
                String dateStr = str(author.get("date"));
                if (dateStr != null && !dateStr.isBlank()) {
                    try { date = Instant.parse(dateStr); }
                    catch (Exception ignored) {}
                }
            }
        }
        return new CommitDto(sha, msg, authorName, authorEmail, date, html);
    }

    private static Map<String,Object> cast(Object o) {
        if (o instanceof Map) return (Map<String,Object>) o;
        return null;
    }


    private static String str(Object o) {
        return o == null ? null : String.valueOf(o);
    }
}
