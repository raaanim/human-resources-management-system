package aitho.ranim.hrms.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EmployeeRequest(
        @NotBlank(message = "First name is required")
        @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
        String firstName,
        @NotBlank(message = "Last name is required")
        @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
        String lastName,
        LocalDate dateOfBirth,
        String gender,
        @NotBlank
        String nationality,
        @NotBlank
        String birthPlace,
        @Size(min = 4, max = 15)
        @NotBlank
        @Pattern(regexp = "^[0-9+\\- ]+$", message = "Invalid phone number")
        String phoneNumber,
        @NotBlank
        @Email(message = "Email should be valid")
        String email,
        @NotBlank
        @Size(min = 8, max = 100)
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]).{8,}$",
                message = "Minimo 8 caratteri, maiuscole, minuscole, numero e simbolo"
        )
        String password,
        @NotBlank
        @Email(message = "Email should be valid")
        String personalEmail,
        @NotBlank
        String address,
        @NotBlank
        String city,
        @NotBlank
        String country,
        @NotBlank
        @Size(min =  5, max = 10, message = "Postal code must be between 5 and 10 characters")
        String postalCode,
        @NotBlank
        String province,
        @NotBlank
        String workLocation
) {

}
