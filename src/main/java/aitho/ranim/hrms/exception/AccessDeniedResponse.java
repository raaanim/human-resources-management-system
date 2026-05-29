package aitho.ranim.hrms.exception;

import org.springframework.http.HttpStatusCode;

public record AccessDeniedResponse(
        String timestamp,
        HttpStatusCode status,
        String message,
        String error
) {
}
