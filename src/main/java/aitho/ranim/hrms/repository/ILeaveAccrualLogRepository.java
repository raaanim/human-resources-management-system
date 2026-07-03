package aitho.ranim.hrms.repository;

import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.LeaveAccrualLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILeaveAccrualLogRepository extends JpaRepository<LeaveAccrualLog, Long> {
    boolean existsByEmployeeAndAccrualMonthAndAccrualYear(
            Employee employee,
            Integer accrualMonth,
            Integer accrualYear
    );
    List<LeaveAccrualLog> findByEmployeeIdOrderByAccrualYearDescAccrualMonthDesc(Long employeeId);
}
