package aitho.ranim.hrms.exception;

import org.springframework.http.HttpStatusCode;

public class LoginException extends RuntimeException {
    HttpStatusCode statusCode;
    String path;

    public LoginException(String message,  HttpStatusCode statusCode,  String path) {
            super(message);
            this.statusCode = statusCode;
            this.path = path;
    }
}
