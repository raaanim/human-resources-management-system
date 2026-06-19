package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.TimeEntryRequest;
import aitho.ranim.hrms.dto.TimeEntryResponse;

public interface ITimeEntryService {
    TimeEntryResponse addHours(TimeEntryRequest timeEntryRequest);
}
