package aitho.ranim.hrms.exception;

import org.springframework.http.HttpStatusCode;

public class LeaveRequestException extends RuntimeException {
    HttpStatusCode statusCode;
    String path;

    public LeaveRequestException(String message,  HttpStatusCode statusCode, String path) {
        super(message);
        this.statusCode = statusCode;
        this.path = path;
    }
}
