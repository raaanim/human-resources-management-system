package aitho.ranim.hrms.dto.employeeDto;
import java.time.LocalDate;

public record CreateEmployeeResponse(
         LocalDate dateOfCreation,
         String message
) {
}
