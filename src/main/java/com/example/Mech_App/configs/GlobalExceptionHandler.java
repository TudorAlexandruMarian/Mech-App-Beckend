package com.example.Mech_App.configs;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomResponseStatusException.class)
    public ResponseEntity<ApiCustomResponse> handleException(CustomResponseStatusException ex) {
        ex.printStackTrace();


        ApiCustomResponse errorResponse = new ApiCustomResponse(
                Instant.now(),
                ex.getStatus().value(),
                HttpStatus.valueOf(ex.getStatus().value()).getReasonPhrase(),
                ex.getCustomMessage()
        );
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiCustomResponse> handleIOException(IOException ex) {
        ex.printStackTrace();
        ApiCustomResponse errorResponse = new ApiCustomResponse(
                Instant.now(),
                400,
                HttpStatus.valueOf(400).getReasonPhrase(),
                ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
