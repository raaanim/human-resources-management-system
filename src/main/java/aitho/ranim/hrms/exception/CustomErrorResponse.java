package aitho.ranim.hrms.exception;

import org.springframework.http.HttpStatusCode;

public record CustomErrorResponse(
        String timestamp,
        HttpStatusCode status,
        String message,
        String error,
        String path
) {
}
