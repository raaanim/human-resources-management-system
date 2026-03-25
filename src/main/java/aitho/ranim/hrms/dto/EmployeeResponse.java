package aitho.ranim.hrms.dto;
import java.time.LocalDate;

public record EmployeeResponse(
         LocalDate dateOfCreation,
         String result
) {
}
