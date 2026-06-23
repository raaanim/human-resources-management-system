package aitho.ranim.hrms.validation;

import aitho.ranim.hrms.dto.ProjectRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProjectDatesValidator implements ConstraintValidator<ValidProjectDates, ProjectRequest> {

    @Override
    public boolean isValid(ProjectRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        if (request.endDate() == null) {
            return true;
        }

        if (request.startDate() == null) {
            return true;
        }

        return !request.endDate().isBefore(request.startDate());
    }
}

