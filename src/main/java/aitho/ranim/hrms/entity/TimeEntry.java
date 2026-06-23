package aitho.ranim.hrms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(
        name = "time-entry",
        uniqueConstraints = {
            @UniqueConstraint(
                    columnNames = {"employee_id", "project_id", "date"}
            )
})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class TimeEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Employee employee;
    @ManyToOne
    private Project project;
    private LocalDate date;
    private BigDecimal hoursWorked;
    private String description;
    private LocalDate createdAt;

}
