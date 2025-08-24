package com.example.githubconnector.advice;

import com.example.githubconnector.dto.ErrorResponse;
import com.example.githubconnector.exception.GithubConnectorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(
            400,
            "Bad Request",
             ex.getMessage()
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArg(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(
            400,
            "Bad Request",
            ex.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(
                500,
                "Internal Server Error",
                ex.getMessage()
            ));
    }

    @ExceptionHandler(GithubConnectorException.class)
    public ResponseEntity<ErrorResponse> handleGithubConnectorException(GithubConnectorException ex){
        return ResponseEntity.status(ex.getStatus()).body(
                new ErrorResponse(
                        ex.getStatus().value(),
                        ex.getStatus().name(),
                        ex.getMessage()
                )
        );
    }
}
