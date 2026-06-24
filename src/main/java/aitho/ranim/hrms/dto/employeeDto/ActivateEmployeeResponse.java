package aitho.ranim.hrms.dto.employeeDto;

import java.time.LocalDate;

public record ActivateEmployeeResponse(
        LocalDate dateOfCreation,
        String message

) {
}
