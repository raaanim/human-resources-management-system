package aitho.ranim.hrms.utils;

import aitho.ranim.hrms.dto.timeEntryDto.TimeEntryResponse;
import aitho.ranim.hrms.entity.TimeEntry;
import lombok.experimental.UtilityClass;



@UtilityClass
public class TimeEntryUtils {

    public TimeEntryResponse toTimeEntryDetailResponse(TimeEntry timeEntry) {
        return new TimeEntryResponse(
                timeEntry.getId(),
                timeEntry.getEmployee().getFirstName() + " " + timeEntry.getEmployee().getLastName(),
                timeEntry.getProject().getName(),
                timeEntry.getProject().getId(),
                timeEntry.getDate(),
                timeEntry.getHoursWorked(),
                timeEntry.getDescription(),
                timeEntry.getCreatedAt()
        );
    }
}
