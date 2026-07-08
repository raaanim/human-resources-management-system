package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestResponse;
import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestReviewRequest;
import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestSubmitRequest;

import java.util.List;

public interface ILeaveRequestService {
    LeaveRequestResponse submitRequest(LeaveRequestSubmitRequest request);
    LeaveRequestResponse reviewRequest(Long id, LeaveRequestReviewRequest request);
    void cancelRequest(Long id);
    List<LeaveRequestResponse> getAllMyLeaveRequests();
    List<LeaveRequestResponse> getAllPendingLeaveRequestsForAdmin();
    List<LeaveRequestResponse> getAllLeaveRequestsMadeByEmployee(Long employeeId);
}
