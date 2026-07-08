package aitho.ranim.hrms.repository;

import aitho.ranim.hrms.dto.leaveRequestDto.LeaveRequestResponse;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.LeaveBalance;
import aitho.ranim.hrms.entity.LeaveRequest;
import aitho.ranim.hrms.enums.LeaveRequestsStatus;
import aitho.ranim.hrms.enums.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ILeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    Optional<LeaveBalance> findByEmployeeAndLeaveType(
            Employee employee,
            LeaveType leaveType
    );
    List<LeaveRequest> findByEmployee_Email(String email);
    List<LeaveRequest>findByStatus(LeaveRequestsStatus status);
    List<LeaveRequest> findByEmployee_Id(Long employeeId);
}
