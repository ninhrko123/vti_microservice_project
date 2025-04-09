package vti.api_gateway.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import vti.api_gateway.response.AuthenticationResponse;

@RestControllerAdvice
public class HandleException extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = { ValidationException.class })
    public ResponseEntity<Object> handleException(ValidationException ex) {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return new ResponseEntity<>(authenticationResponse, HttpStatus.UNAUTHORIZED);
    }

}
