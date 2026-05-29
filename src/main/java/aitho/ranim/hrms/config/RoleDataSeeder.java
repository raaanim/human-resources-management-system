package aitho.ranim.hrms.config;

import aitho.ranim.hrms.entity.Role;
import aitho.ranim.hrms.enums.RoleName;
import aitho.ranim.hrms.repository.IRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleDataSeeder implements CommandLineRunner {

    private final IRoleRepository roleRepository;
    public RoleDataSeeder(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName(RoleName.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(RoleName.ROLE_ADMIN));
        }

        if (roleRepository.findByName(RoleName.ROLE_HR).isEmpty()) {
            roleRepository.save(new Role(RoleName.ROLE_HR));
        }

        if (roleRepository.findByName(RoleName.ROLE_EMPLOYEE).isEmpty()) {
            roleRepository.save(new Role(RoleName.ROLE_EMPLOYEE));
        }
    }
}
