package aitho.ranim.hrms.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TimeEntryException extends RuntimeException {
    HttpStatusCode statusCode;
    String path;

    public TimeEntryException(String message, HttpStatus statusCode, String path) {
        super(message);
        this.statusCode = statusCode;
        this.path = path;

    }
}
