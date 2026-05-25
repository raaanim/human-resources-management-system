package aitho.ranim.hrms.service.security.impl;

import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IEmployeeRepository employeeRepository;

    public UserDetailsServiceImpl(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return User.withUsername(employee.getEmail())
                .password(employee.getPassword())
                .authorities(employee.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .collect(Collectors.toList())
                )
                .build();
    }
}
