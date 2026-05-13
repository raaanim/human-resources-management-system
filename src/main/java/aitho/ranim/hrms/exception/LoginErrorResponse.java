package aitho.ranim.hrms.exception;

import org.springframework.http.HttpStatusCode;

public record LoginErrorResponse(
        String timestamp,
        HttpStatusCode status,
        String message,
        String error
) {
}
