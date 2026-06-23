package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.ProjectHoursReportResponse;
import aitho.ranim.hrms.dto.TimeEntryRequest;
import aitho.ranim.hrms.dto.TimeEntryResponse;

import java.util.List;

public interface ITimeEntryService {
    TimeEntryResponse addHours(TimeEntryRequest timeEntryRequest);
    List<TimeEntryResponse> getMyEntries(String month);
    ProjectHoursReportResponse getProjectReport(Long projectId);
    void deleteEntry(Long id);
}
