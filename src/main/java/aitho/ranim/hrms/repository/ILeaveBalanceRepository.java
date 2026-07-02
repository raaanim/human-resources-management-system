package aitho.ranim.hrms.repository;

import aitho.ranim.hrms.entity.LeaveBalance;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ILeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    Optional<LeaveBalance> findByEmployee_Id(Long employeeId);

    Optional<LeaveBalance> findByEmployee_Id(Long employeeId, Sort sort, Limit limit);
}
