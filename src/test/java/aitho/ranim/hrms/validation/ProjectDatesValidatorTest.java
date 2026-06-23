package aitho.ranim.hrms.validation;

import aitho.ranim.hrms.dto.ProjectRequest;
import aitho.ranim.hrms.enums.ProjectStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProjectDatesValidatorTest {

    private ProjectDatesValidator projectDatesValidator;


    @BeforeEach
    void setUp() {
        projectDatesValidator = new ProjectDatesValidator();
    }

    @Test
    void shouldReturnTrueWhenEndDateIsNull() {
        ProjectRequest request = new ProjectRequest(
                "Website Redesign",
                "Redesign corporate website",
                "ACME",
                LocalDate.of(2024, 12, 1),
                ProjectStatus.IN_PROGRESS,
                null,
                BigDecimal.valueOf(10000)
        );

        assertTrue(projectDatesValidator.isValid(request, null));
    }

    @Test
    void shouldReturnTrueWhenEndDateIsAfterStartDate() {
        ProjectRequest request = new ProjectRequest(
                "Website Redesign",
                "Redesign corporate website",
                "ACME",
                LocalDate.of(2024, 12, 1),
                ProjectStatus.IN_PROGRESS,
                LocalDate.of(2024, 12, 10),
                BigDecimal.valueOf(10000)
        );

        assertTrue(projectDatesValidator.isValid(request, null));
    }

    @Test
    void shouldReturnTrueWhenEndDateIsEqualToStartDate() {
        ProjectRequest request = new ProjectRequest(
                "Website Redesign",
                "Redesign corporate website",
                "ACME",
                LocalDate.of(2024, 12, 1),
                ProjectStatus.IN_PROGRESS,
                LocalDate.of(2024, 12, 1),
                BigDecimal.valueOf(10000)
        );

        assertTrue(projectDatesValidator.isValid(request, null));
    }

    @Test
    void shouldReturnFalseWhenEndDateIsBeforeStartDate() {
        ProjectRequest request = new ProjectRequest(
                "Website Redesign",
                "Redesign corporate website",
                "ACME",
                LocalDate.of(2024, 12, 1),
                ProjectStatus.IN_PROGRESS,
                LocalDate.of(2024, 1, 1),
                BigDecimal.valueOf(10000)
        );

        assertFalse(projectDatesValidator.isValid(request, null));
    }
}
