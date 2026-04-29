package aitho.ranim.hrms.dto;

import java.time.LocalDate;

public record EmployeeDetailResponse(
        Long id,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String gender,
        String nationality,
        String birthPlace,
        String phoneNumber,
        String address,
        String city,
        String province,
        String postalCode,
        String country,
        String workLocation
) {
}
