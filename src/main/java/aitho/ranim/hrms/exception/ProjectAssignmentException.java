package aitho.ranim.hrms.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectAssignmentException extends RuntimeException {
    HttpStatusCode statusCode;
    String path;

    public ProjectAssignmentException(String message, HttpStatusCode statusCode, String path) {
        super(message);
        this.statusCode = statusCode;
        this.path = path;
    }
}
