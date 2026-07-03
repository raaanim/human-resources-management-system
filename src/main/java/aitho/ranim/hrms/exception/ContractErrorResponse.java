package aitho.ranim.hrms.exception;

import org.springframework.http.HttpStatusCode;

public record ContractErrorResponse(
        String timestamp,
        HttpStatusCode status,
        String message,
        String error,
        String path
) {
}
