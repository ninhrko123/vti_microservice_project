package vti.auth_service.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends Exception {
    private final HttpStatus status;

    public CustomException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
