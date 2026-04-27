package aitho.ranim.hrms.exception.handler;

import aitho.ranim.hrms.exception.CustomErrorResponse;
import aitho.ranim.hrms.exception.EmailCustomException;
import aitho.ranim.hrms.exception.EmailErrorResponse;
import aitho.ranim.hrms.exception.EmployeeException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;
import java.util.stream.Collectors;



@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { EmployeeException.class })
    public ResponseEntity<CustomErrorResponse> handleException(EmployeeException e, HttpServletRequest request) {
        log.error("An error occurred: {}", e.getMessage());

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDateTime.now().toString(),
                e.getStatusCode(),
                e.getMessage(),
                e.getCause() != null ? e.getCause().toString() : "No additional error details",
                request.getRequestURI()
        );

        // Return a generic error response to the client
        return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(value = {EmailCustomException.class})
    public ResponseEntity<EmailErrorResponse> handleEmailException(EmailCustomException e) {
        log.error("An email error occurred: {}", e.getMessage());

        EmailErrorResponse errorResponse = new EmailErrorResponse(
                LocalDateTime.now().toString(),
                e.getStatusCode(),
                e.getMessage(),
                e.getCause() != null ? e.getCause().toString() : "No additional error details"
        );
        return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
    }



    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("Found an error: {}", e.getMessage());

        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                LocalDateTime.now().toString(),
                e.getStatusCode(),
                message,
                e.getCause() != null ? e.getCause().toString() : "Validation error",
                request.getRequestURI()
        );

        // Return a generic error response to the client
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}

