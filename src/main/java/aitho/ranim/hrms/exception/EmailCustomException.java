package aitho.ranim.hrms.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmailCustomException extends RuntimeException {
    HttpStatusCode statusCode;
    String Timestamp;


    public EmailCustomException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
