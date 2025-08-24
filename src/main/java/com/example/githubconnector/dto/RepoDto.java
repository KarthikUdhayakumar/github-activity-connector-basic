package com.example.githubconnector.dto;

import java.util.Map;

public class RepoDto {
    private String name;
    private String fullName;
    private String htmlUrl;
    private String defaultBranch;

    public RepoDto() {}

    public RepoDto(String name, String fullName,
                   String htmlUrl, String defaultBranch) {
        this.name = name;
        this.fullName = fullName;
        this.htmlUrl = htmlUrl;
        this.defaultBranch = defaultBranch;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getHtmlUrl() { return htmlUrl; }
    public void setHtmlUrl(String htmlUrl) { this.htmlUrl = htmlUrl; }

    public String getDefaultBranch() { return defaultBranch; }
    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public static RepoDto toRepoDto(Map<String, Object> r){
        String name = str(r.get("name"));
        String full = str(r.get("full_name"));
        String html = str(r.get("html_url"));
        String defb = str(r.get("default_branch"));
        return new RepoDto(name, full, html, defb);
    }

    private static String str(Object o) {
        return o == null ? null : String.valueOf(o);
    }
}
