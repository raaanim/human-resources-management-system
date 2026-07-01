package aitho.ranim.hrms.exception;

import org.springframework.http.HttpStatus;

public class LeaveAccrualException extends RuntimeException {
     HttpStatus status;

    public LeaveAccrualException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
