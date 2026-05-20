package aitho.ranim.hrms.entity;

import aitho.ranim.hrms.enums.RoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Enumerated(EnumType.STRING)
    RoleName name;
}
