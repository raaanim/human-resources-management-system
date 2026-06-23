package aitho.ranim.hrms.repository;

import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.TimeEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ITimeEntryRepository extends JpaRepository<TimeEntry, Long> {
    boolean existsByEmployeeIdAndProjectIdAndDate(Long employeeId, Long projectId, LocalDate date);

    List<TimeEntry> findByEmployeeAndDateBetween(Employee employee, LocalDate start, LocalDate end);

    List<TimeEntry> findByProjectId(Long projectId);

    @Query("""
    SELECT COALESCE(SUM(t.hoursWorked), 0)
    FROM TimeEntry t
    WHERE t.project.id = :projectId
""")
    BigDecimal getTotalHoursByProject(@Param("projectId") Long projectId);
}
