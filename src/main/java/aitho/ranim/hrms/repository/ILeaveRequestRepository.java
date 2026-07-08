package aitho.ranim.hrms.repository;

import aitho.ranim.hrms.entity.LeaveRequest;
import aitho.ranim.hrms.enums.LeaveRequestsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ILeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployee_Email(String email);
    List<LeaveRequest>findByStatus(LeaveRequestsStatus status);
    List<LeaveRequest> findByEmployee_Id(Long employeeId);
}
