package aitho.ranim.hrms.repository;

import aitho.ranim.hrms.entity.Project;
import aitho.ranim.hrms.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByStatus(ProjectStatus status, Pageable pageable); // filters projects by status
    List<Project> findByClientNameContainingIgnoreCase(String clientName); // to search a client by name
}
