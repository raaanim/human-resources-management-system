package aitho.ranim.hrms.dto;

public record EmployeeRequest(
        String firstName,
        String lastName,
        String dateOfBirth,
        String gender,
        String nationality,
        String birthPlace,
        String phoneNumber,
        String workEmail,
        String password,
        String personalEmail,
        String address,
        String city,
        String country,
        String postalCode,
        String province,
        String workLocation,
        String status
) {

}
