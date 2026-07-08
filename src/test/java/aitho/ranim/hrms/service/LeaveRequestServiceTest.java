package aitho.ranim.hrms.service;

import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestResponse;
import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestReviewRequest;
import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestSubmitRequest;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.LeaveBalance;
import aitho.ranim.hrms.entity.LeaveRequest;
import aitho.ranim.hrms.enums.LeaveRequestsStatus;
import aitho.ranim.hrms.enums.LeaveType;
import aitho.ranim.hrms.exception.EmployeeException;
import aitho.ranim.hrms.exception.LeaveBalanceException;
import aitho.ranim.hrms.exception.LeaveRequestException;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.repository.ILeaveBalanceRepository;
import aitho.ranim.hrms.repository.ILeaveRequestRepository;
import aitho.ranim.hrms.service.impl.EmailService;
import aitho.ranim.hrms.service.impl.LeaveRequestService;
import aitho.ranim.hrms.utils.BusinessDaysCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeaveRequestServiceTest {
            @Mock
            private ILeaveRequestRepository leaveRequestRepository;
            @Mock
            private IEmployeeRepository employeeRepository;
            @Mock
            private ILeaveBalanceRepository leaveBalanceRepository;
            @Mock
            private BusinessDaysCalculator businessDaysCalculator;
            @Mock
            private EmailService emailService;
            @InjectMocks
            private LeaveRequestService service;

            private Employee employee;
            private LeaveBalance leaveBalance;

            @BeforeEach
            void setup() {
                employee = new Employee();
                employee.setId(1L);
                employee.setEmail("mario@test.com");

                leaveBalance = new LeaveBalance();
                leaveBalance.setAccruedDays(new BigDecimal("20"));
                leaveBalance.setUsedDays(BigDecimal.ZERO);
                leaveBalance.setPendingDays(BigDecimal.ZERO);

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                                new UsernamePasswordAuthenticationToken(
                                        "mario@test.com",
                                        null
                                )
                        );
            }

            @Test
            void submitRequest_shouldCreateLeaveRequest_whenBalanceIsEnough() {
                LeaveRequestSubmitRequest request =
                        new LeaveRequestSubmitRequest(
                                LeaveType.CASUAL_LEAVE,
                                LocalDate.of(2026,1,10),
                                LocalDate.of(2026,1,12),
                                "Casual leave"
                        );

                when(employeeRepository.findByEmail("mario@test.com"))
                        .thenReturn(Optional.of(employee));

                when(businessDaysCalculator.calculate(
                        any(),
                        any()
                )).thenReturn(new BigDecimal("3"));

                when(leaveBalanceRepository.findByEmployee_Id(1L))
                        .thenReturn(Optional.of(leaveBalance));

                LeaveRequest saved = new LeaveRequest();
                saved.setId(10L);
                saved.setEmployee(employee);
                saved.setTotalDays(new BigDecimal("3"));
                saved.setStatus(LeaveRequestsStatus.PENDING);

                when(leaveRequestRepository.save(any()))
                        .thenReturn(saved);

                LeaveRequestResponse response =
                        service.submitRequest(request);

                assertNotNull(response);

                verify(leaveBalanceRepository)
                        .save(leaveBalance);

                verify(leaveRequestRepository)
                        .save(any(LeaveRequest.class));

                verify(emailService)
                        .sendLeaveRequestSubmitted(saved);
            }

            @Test
            void submitRequest_shouldThrowException_whenBalanceNotEnough() {
                LeaveRequestSubmitRequest request =
                        new LeaveRequestSubmitRequest(
                                LeaveType.CASUAL_LEAVE,
                                LocalDate.now(),
                                LocalDate.now().plusDays(10),
                                "Casual leave"
                        );
                when(employeeRepository.findByEmail(anyString()))
                        .thenReturn(Optional.of(employee));

                when(businessDaysCalculator.calculate(any(),any()))
                        .thenReturn(new BigDecimal("30"));

                when(leaveBalanceRepository.findByEmployee_Id(1L))
                        .thenReturn(Optional.of(leaveBalance));

                assertThrows(
                        LeaveBalanceException.class,
                        () -> service.submitRequest(request)
                );

                verify(leaveRequestRepository, never()).save(any());
            }

            @Test
            void submitRequest_shouldThrowException_whenEmployeeNotFound() {
                when(employeeRepository.findByEmail(anyString()))
                        .thenReturn(Optional.empty());


                LeaveRequestSubmitRequest request =
                        new LeaveRequestSubmitRequest(
                                LeaveType.CASUAL_LEAVE,
                                LocalDate.now(),
                                LocalDate.now(),
                                "test"
                        );
                assertThrows(
                        EmployeeException.class,
                        () -> service.submitRequest(request)
                );
            }

            @Test
            void reviewRequest_shouldApproveRequest() {
                LeaveRequest leaveRequest = new LeaveRequest();
                leaveRequest.setId(1L);
                leaveRequest.setEmployee(employee);
                leaveRequest.setStatus(LeaveRequestsStatus.PENDING);
                leaveRequest.setTotalDays(new BigDecimal("5"));

                LeaveRequestReviewRequest review =
                        new LeaveRequestReviewRequest(
                                LeaveRequestsStatus.APPROVED,
                                "Approved"
                        );

                Employee reviewer = new Employee();
                reviewer.setId(2L);

                when(leaveRequestRepository.findById(1L))
                        .thenReturn(Optional.of(leaveRequest));

                when(employeeRepository.findByEmail(anyString()))
                        .thenReturn(Optional.of(reviewer));

                when(leaveBalanceRepository.findByEmployee_Id(1L))
                        .thenReturn(Optional.of(leaveBalance));

                when(leaveRequestRepository.save(any()))
                        .thenReturn(leaveRequest);

                LeaveRequestResponse response =
                        service.reviewRequest(1L, review);

                assertEquals(
                        LeaveRequestsStatus.APPROVED,
                        leaveRequest.getStatus()
                );

                assertEquals(
                        new BigDecimal("5"),
                        leaveBalance.getUsedDays()
                );

                verify(leaveBalanceRepository)
                        .save(leaveBalance);

            }

            @Test
            void reviewRequest_shouldRejectRequest() {
                LeaveRequest leaveRequest = new LeaveRequest();
                leaveRequest.setEmployee(employee);
                leaveRequest.setStatus(LeaveRequestsStatus.PENDING);
                leaveRequest.setTotalDays(new BigDecimal(2));
                leaveBalance.setPendingDays(new BigDecimal(2));

                LeaveRequestReviewRequest review =
                        new LeaveRequestReviewRequest(
                                LeaveRequestsStatus.REJECTED,
                                "Not approved"
                        );

                when(leaveRequestRepository.findById(1L))
                        .thenReturn(Optional.of(leaveRequest));


                when(employeeRepository.findByEmail(anyString()))
                        .thenReturn(Optional.of(employee));

                when(leaveBalanceRepository.findByEmployee_Id(1L))
                        .thenReturn(Optional.of(leaveBalance));

                when(leaveRequestRepository.save(any()))
                        .thenReturn(leaveRequest);
                service.reviewRequest(1L, review);

                assertEquals(
                        LeaveRequestsStatus.REJECTED,
                        leaveRequest.getStatus()
                );
                assertEquals(
                        BigDecimal.ZERO,
                        leaveBalance.getPendingDays()
                );
            }

            @Test
            void reviewRequest_shouldFail_whenRequestAlreadyReviewed() {
                LeaveRequest leaveRequest = new LeaveRequest();
                leaveRequest.setStatus(
                        LeaveRequestsStatus.APPROVED
                );
                when(leaveRequestRepository.findById(1L))
                        .thenReturn(Optional.of(leaveRequest));

                LeaveRequestReviewRequest review =
                        new LeaveRequestReviewRequest(
                                LeaveRequestsStatus.REJECTED,
                                ""
                        );
                assertThrows(
                        LeaveRequestException.class,
                        () -> service.reviewRequest(1L, review)
                );

            }

            @Test
            void cancelRequest_shouldDeletePendingRequest() {
                LeaveRequest request = new LeaveRequest();
                request.setStatus(
                        LeaveRequestsStatus.PENDING
                );
                when(leaveRequestRepository.findById(1L))
                        .thenReturn(Optional.of(request));
                service.cancelRequest(1L);
                verify(leaveRequestRepository)
                        .delete(request);
            }

            @Test
            void cancelRequest_shouldFail_whenNotPending() {
                LeaveRequest request = new LeaveRequest();
                request.setStatus(
                        LeaveRequestsStatus.REJECTED
                );
                when(leaveRequestRepository.findById(1L))
                        .thenReturn(Optional.of(request));
                assertThrows(
                        LeaveRequestException.class,
                        () -> service.cancelRequest(1L)
                );
                verify(leaveRequestRepository, never())
                        .delete(any());

            }
}

