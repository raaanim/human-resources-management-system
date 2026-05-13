package aitho.ranim.hrms.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record LoginRequest(
        @Email(message = "Email should be valid")
        String email,
        @Size(min = 8, max = 100)
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]).{8,}$",
                message = "Minimum 8 characters, including uppercase, lowercase, number, and symbol"
        )
        String password
) {
}
