package aitho.ranim.hrms.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UpdateEmployeeRequest(
        @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
        String firstName,
        @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
        String lastName,
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,
        String gender,
        String nationality,
        @Email(message = "Email should be valid")
        String personalEmail,
        String birthPlace,
        @Size(min = 4, max = 15)
        @Pattern(regexp = "^[0-9+\\- ]+$", message = "Invalid phone number")
        String phoneNumber,
        String address,
        String city,
        String country,
        @Size(min =  5, max = 10, message = "Postal code must be between 5 and 10 characters")
        String postalCode,
        String province,
        String workLocation
) {
}
