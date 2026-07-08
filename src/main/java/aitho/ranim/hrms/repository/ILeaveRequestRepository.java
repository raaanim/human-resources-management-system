package aitho.ranim.hrms.repository;

import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.LeaveBalance;
import aitho.ranim.hrms.entity.LeaveRequest;
import aitho.ranim.hrms.enums.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ILeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    Optional<LeaveBalance> findByEmployeeAndLeaveType(
            Employee employee,
            LeaveType leaveType
    );
}
