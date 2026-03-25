package aitho.ranim.hrms.dto;
import java.time.LocalDate;

public record EmployeeResponse(
         Long id,
         String firstName,
         String lastName,
         LocalDate dateOfBirth,
         LocalDate dateOfCreation
) {
}
