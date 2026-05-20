package aitho.ranim.hrms.repository;

import aitho.ranim.hrms.entity.Role;
import aitho.ranim.hrms.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Long> {
     Optional<Role> findByName(RoleName name);
}
