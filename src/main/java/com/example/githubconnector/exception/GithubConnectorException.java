package com.example.githubconnector.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class GithubConnectorException extends RuntimeException{
    final HttpStatus status;

    public GithubConnectorException(final HttpStatus status, final String message){
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
