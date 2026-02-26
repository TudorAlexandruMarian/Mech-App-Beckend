package com.example.Mech_App.configs;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class CustomResponseStatusException extends ResponseStatusException {
    private final String customMessage;
    private final Object data;


    public CustomResponseStatusException(HttpStatus status, String reason) {
        super(status, reason);
        this.customMessage = reason;
        this.data = null;
    }

    public CustomResponseStatusException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
        this.customMessage = reason;
        this.data = null;
    }

    public CustomResponseStatusException(HttpStatus status, String reason, Object data) {
        super(status, reason);
        this.customMessage = reason;
        this.data = data;
    }

    public CustomResponseStatusException(HttpStatus status, String reason, Throwable cause, Object data) {
        super(status, reason, cause);
        this.customMessage = reason;
        this.data = data;
    }

    public CustomResponseStatusException(HttpStatus status, String reason, String customMessage, Object data) {
        super(status, reason);
        this.customMessage = customMessage;
        this.data = data;
    }

    @Override
    public String getMessage() {
        return customMessage;
    }

    public HttpStatus getStatus() {
        return (HttpStatus) getStatusCode();
    }

}
