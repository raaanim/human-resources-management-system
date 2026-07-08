package aitho.ranim.hrms.service.impl;

import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestResponse;
import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestReviewRequest;
import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestSubmitRequest;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.LeaveBalance;
import aitho.ranim.hrms.entity.LeaveRequest;
import aitho.ranim.hrms.enums.LeaveRequestsStatus;
import aitho.ranim.hrms.exception.EmployeeException;
import aitho.ranim.hrms.exception.LeaveBalanceException;
import aitho.ranim.hrms.exception.LeaveRequestException;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.repository.ILeaveBalanceRepository;
import aitho.ranim.hrms.repository.ILeaveRequestRepository;
import aitho.ranim.hrms.service.ILeaveRequestService;
import aitho.ranim.hrms.utils.BusinessDaysCalculator;
import aitho.ranim.hrms.utils.LeaveRequestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;



@Slf4j
@RequiredArgsConstructor
@Service
public class LeaveRequestService implements ILeaveRequestService {
    private final ILeaveRequestRepository leaveRequestRepository;
    private final IEmployeeRepository employeeRepository;
    private final ILeaveBalanceRepository leaveBalanceRepository;
    private final BusinessDaysCalculator businessDaysCalculator;
    private final EmailService emailService;

    @Transactional
    public LeaveRequestResponse submitRequest(LeaveRequestSubmitRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EmployeeException("Employee not found", HttpStatus.NOT_FOUND, "/leave-request/submit"));

        BigDecimal totalDays = businessDaysCalculator.calculate(
                request.startDate(),
                request.endDate()
        );

        LeaveBalance leaveBalance = leaveBalanceRepository
                .findByEmployee_Id(employee.getId())
                .orElseThrow(() ->
                        new LeaveBalanceException("Leave balance not found", HttpStatus.NOT_FOUND, "/leave-request/submit")
                );

        BigDecimal availableDays = getBigDecimal(leaveBalance);

        if (availableDays.compareTo(totalDays) < 0) {
            throw new LeaveBalanceException(
                    "Insufficient available leave days",
                    HttpStatus.BAD_REQUEST,
                    "/leave-request/submit"
            );
        }

        leaveBalance.setPendingDays(
                safe(leaveBalance.getPendingDays()).add(totalDays)
        );

        leaveBalanceRepository.save(leaveBalance);

        LeaveRequest leaveRequest =
                LeaveRequestUtils.toSubmitLeaveRequest(
                        request,
                        employee,
                        totalDays
                );

        LeaveRequest savedRequest =
                leaveRequestRepository.save(leaveRequest);

        try {
            emailService.sendLeaveRequestSubmitted(savedRequest);
        }catch(Exception e) {
            log.error("Failed to send email notification: {}", e.getMessage());
        }

         return LeaveRequestUtils.toLeaveRequestResponse(savedRequest);
    }

    @Transactional
    public LeaveRequestResponse reviewRequest(Long id, LeaveRequestReviewRequest request) {

            LeaveRequest leaveRequest =
                    leaveRequestRepository.findById(id)
                            .orElseThrow(() ->
                                    new LeaveRequestException(
                                            "Leave request not found", HttpStatus.NOT_FOUND, "/leave-request"
                                    )
                            );

            if (leaveRequest.getStatus() != LeaveRequestsStatus.PENDING) {
                throw new LeaveRequestException(
                        "Only pending requests can be reviewed", HttpStatus.BAD_REQUEST, "/leave-request"
                );
            }

            Employee reviewer = employeeRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()
            ).orElseThrow(() -> new EmployeeException("Reviewer not found", HttpStatus.NOT_FOUND, "/leave-request"));

            Employee employee = leaveRequest.getEmployee();

            LeaveBalance leaveBalance =
                    leaveBalanceRepository.findByEmployee_Id(employee.getId())
                            .orElseThrow(() ->
                                    new LeaveBalanceException(
                                            "Leave balance not found",  HttpStatus.NOT_FOUND, "/leave-request"
                                    )
                            );

            BigDecimal days = leaveRequest.getTotalDays();

        if (LeaveRequestsStatus.APPROVED.equals(request.status())) {

            leaveBalance.setPendingDays(
                    safe(leaveBalance.getPendingDays())
                            .subtract(days)
            );

            leaveBalance.setUsedDays(
                    safe(leaveBalance.getUsedDays())
                            .add(days)
            );

            leaveRequest.setStatus(LeaveRequestsStatus.APPROVED);

        } else if (LeaveRequestsStatus.REJECTED.equals(request.status())) {

            leaveBalance.setPendingDays(
                    safe(leaveBalance.getPendingDays())
                            .subtract(days)
            );

            leaveRequest.setStatus(LeaveRequestsStatus.REJECTED);

        } else {

            throw new LeaveRequestException(
                    "Review status must be APPROVED or REJECTED",
                    HttpStatus.BAD_REQUEST,
                    "/leave-request"
            );
        }

            leaveBalanceRepository.save(leaveBalance);
            leaveRequest.setReviewer(reviewer);
            leaveRequest.setReviewerNotes(request.reviewerNotes());
            leaveRequest.setReviewedAt(LocalDate.now());

            LeaveRequest saved =
                    leaveRequestRepository.save(leaveRequest);

        try {
            emailService.sendLeaveRequestSubmitted(leaveRequest);
        }catch(Exception e) {
            log.error("Error: email has not been sent: {}", e.getMessage());
        }

            return LeaveRequestUtils.toLeaveRequestResponse(saved);
        }

    private static BigDecimal getBigDecimal(LeaveBalance leaveBalance) {
        BigDecimal accruedDays = leaveBalance.getAccruedDays() != null
                ? leaveBalance.getAccruedDays()
                : BigDecimal.ZERO;

        BigDecimal usedDays = leaveBalance.getUsedDays() != null
                ? leaveBalance.getUsedDays()
                : BigDecimal.ZERO;

        BigDecimal pendingDays = leaveBalance.getPendingDays() != null
                ? leaveBalance.getPendingDays()
                : BigDecimal.ZERO;

        BigDecimal availableDays =
                accruedDays
                        .subtract(usedDays)
                        .subtract(pendingDays);
        return availableDays;
    }

    private BigDecimal safe(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
    }

