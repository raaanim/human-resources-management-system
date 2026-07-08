package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestResponse;
import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestReviewRequest;
import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestSubmitRequest;

public interface ILeaveRequestService {
    LeaveRequestResponse submitRequest(LeaveRequestSubmitRequest request);
    LeaveRequestResponse reviewRequest(Long id, LeaveRequestReviewRequest request);
}
