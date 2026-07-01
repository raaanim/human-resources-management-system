package aitho.ranim.hrms.repository;

import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ILeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    Optional<LeaveBalance> findByEmployee(Employee employee);

}
