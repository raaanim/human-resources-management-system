package aitho.ranim.hrms.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record LoginRequest(
        @Email(message = "Email should be valid")
        String email,
        String password
) {
}
