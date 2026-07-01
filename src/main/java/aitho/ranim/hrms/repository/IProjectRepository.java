package aitho.ranim.hrms.repository;

import aitho.ranim.hrms.entity.Project;
import aitho.ranim.hrms.entity.ProjectAssignment;
import aitho.ranim.hrms.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findByStatus(ProjectStatus status, Pageable pageable); // filters projects by status
}
