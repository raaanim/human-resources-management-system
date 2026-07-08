package aitho.ranim.hrms.exception;

import org.springframework.http.HttpStatusCode;

public record LeaveBalanceErrorResponse(
        String timestamp,
        HttpStatusCode status,
        String message,
        String error,
        String path
) {
}
