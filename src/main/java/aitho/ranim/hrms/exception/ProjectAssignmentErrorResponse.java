package aitho.ranim.hrms.exception;

import org.springframework.http.HttpStatusCode;

public record ProjectAssignmentErrorResponse(
        String timestamp,
        HttpStatusCode status,
        String message,
        String error,
        String path
) {
}
