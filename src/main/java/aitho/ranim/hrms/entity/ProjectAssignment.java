package aitho.ranim.hrms.entity;

import aitho.ranim.hrms.enums.AssignmentRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "project-assignment")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ProjectAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Employee employee;
    @ManyToOne
    private Project project;
    private AssignmentRole role;
    private LocalDate startDate;
    @Column(nullable = true)
    private LocalDate endDate;
    private LocalDate assignedAt;
}
