package aitho.ranim.hrms.exception;

import org.springframework.http.HttpStatusCode;

public record ProjectErrorResponse(
        String timestamp,
        HttpStatusCode status,
        String message,
        String error,
        String path
) {
}
