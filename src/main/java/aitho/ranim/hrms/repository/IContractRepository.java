package aitho.ranim.hrms.repository;

import aitho.ranim.hrms.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByEmployeeIdOrderByStartDateDesc(Long employeeId);
    Optional<Contract> findFirstByEmployeeIdAndActiveTrue(Long employeeId);
    List<Contract>findByActiveTrueAndEndDateBetween(LocalDate start, LocalDate end);

    @Modifying
    @Query("""
        update Contract c
        set c.active = false
        where c.employee.id = :employeeId
    """)
    void deactivateByEmployeeId(Long employeeId);
}
