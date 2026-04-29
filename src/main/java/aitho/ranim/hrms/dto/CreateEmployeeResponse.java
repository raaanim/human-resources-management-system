package aitho.ranim.hrms.dto;
import java.time.LocalDate;

public record CreateEmployeeResponse(
         LocalDate dateOfCreation,
         String message
) {
}
