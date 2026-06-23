package aitho.ranim.hrms.repository;

import aitho.ranim.hrms.entity.ProjectAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IProjectAssignmentRepository extends JpaRepository<ProjectAssignment, Long> {
    List<ProjectAssignment> findByProjectId(Long projectId);
    List <ProjectAssignment> findByEmployeeId(Long employeeId);
    Optional<ProjectAssignment> findByEmployeeIdAndProjectIdAndEndDateIsNull(Long employeeId, Long projectId);
    boolean existsByEmployeeIdAndProjectIdAndEndDateIsNull(Long employeeId, Long projectId);
}
