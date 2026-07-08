package aitho.ranim.hrms.controller;

import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestResponse;
import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestReviewRequest;
import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestSubmitRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

public interface ILeaveRequestController {
    ResponseEntity<LeaveRequestResponse> submitLeaveRequest(@Valid @RequestBody LeaveRequestSubmitRequest request);
    ResponseEntity<LeaveRequestResponse> reviewLeaveRequest(@PathVariable Long id, LeaveRequestReviewRequest request);
    ResponseEntity<List<LeaveRequestResponse>> getMyLeaveRequests();
    ResponseEntity<List<LeaveRequestResponse>> getAllPendingLeaveRequests();
    ResponseEntity<List<LeaveRequestResponse>> getAllLeaveRequestsByEmployee(
            @PathVariable Long employeeId);
}