package aitho.ranim.hrms.config;

import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.Role;
import aitho.ranim.hrms.enums.RoleName;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.repository.IRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class EmployeeDataSeeder implements CommandLineRunner {

        private final IEmployeeRepository employeeRepository;
        private final IRoleRepository roleRepository;
        private final PasswordEncoder passwordEncoder;

        public EmployeeDataSeeder(IEmployeeRepository employeeRepository,
                                  IRoleRepository roleRepository,
                                  PasswordEncoder passwordEncoder) {
            this.employeeRepository = employeeRepository;
            this.roleRepository = roleRepository;
            this.passwordEncoder = passwordEncoder;
        }

        @Override
        public void run(String... args) {

            if (employeeRepository.findByEmail("admin@test.com").isEmpty()) {

                Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

                Employee admin = new Employee();
                admin.setFirstName("Admin");
                admin.setLastName("User");
                admin.setEmail("admin@test.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setStatus("ACTIVE");

                admin.setRoles(Set.of(adminRole));

                employeeRepository.save(admin);
            }

            if (employeeRepository.findByEmail("hr@test.com").isEmpty()) {

                Role hrRole = roleRepository.findByName(RoleName.ROLE_HR)
                        .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

                Employee hr = new Employee();
                hr.setFirstName("Hr");
                hr.setLastName("User");
                hr.setEmail("hr@test.com");
                hr.setPassword(passwordEncoder.encode("hr123"));
                hr.setStatus("ACTIVE");

                hr.setRoles(Set.of(hrRole));

                employeeRepository.save(hr);
            }
        }
    }

