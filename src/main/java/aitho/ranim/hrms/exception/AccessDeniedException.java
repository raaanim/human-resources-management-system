package aitho.ranim.hrms.exception;

import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;

@EqualsAndHashCode(callSuper = true)
public class AccessDeniedException extends RuntimeException {
    HttpStatusCode statusCode;
    String Timestamp;

    public AccessDeniedException(String message,  HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
