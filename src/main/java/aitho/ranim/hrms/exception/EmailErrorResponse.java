package aitho.ranim.hrms.exception;

import org.springframework.http.HttpStatusCode;

public record EmailErrorResponse(
        String timestamp,
        HttpStatusCode status,
        String message,
        String error
) {
}
