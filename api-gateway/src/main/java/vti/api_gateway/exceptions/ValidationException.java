package vti.api_gateway.exceptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {
    private HttpStatus status;
    private String message;

    public ValidationException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }
}
