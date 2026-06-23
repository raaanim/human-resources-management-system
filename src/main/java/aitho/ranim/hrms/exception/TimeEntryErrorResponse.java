package aitho.ranim.hrms.exception;

import org.springframework.http.HttpStatus;

public record TimeEntryErrorResponse(
        String message,
        HttpStatus statusCode,
        String path
) {
}
