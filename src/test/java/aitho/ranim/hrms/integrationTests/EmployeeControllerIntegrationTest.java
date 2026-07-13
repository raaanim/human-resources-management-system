package aitho.ranim.hrms.integrationTests;

import aitho.ranim.hrms.dto.employeeDto.EmployeeRequest;
import aitho.ranim.hrms.dto.security.LoginRequest;
import aitho.ranim.hrms.entity.Employee;
import aitho.ranim.hrms.entity.Role;
import aitho.ranim.hrms.enums.RoleName;
import aitho.ranim.hrms.repository.IEmployeeRepository;
import aitho.ranim.hrms.repository.IRoleRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Set;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IEmployeeRepository employeeRepository;

    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup(){

        employeeRepository.deleteAll();

        Role adminRole = roleRepository
                .findByName(RoleName.ROLE_ADMIN)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(RoleName.ROLE_ADMIN);
                    return roleRepository.save(role);
                });
        Employee admin = new Employee();

        admin.setFirstName("Admin");
        admin.setLastName("Test");
        admin.setEmail("admin@test.com");

        admin.setPassword(
                passwordEncoder.encode("password")
        );

        admin.setStatus("ACTIVE");

        admin.setRoles(Set.of(adminRole));

        employeeRepository.save(admin);
    }

    @Test
    void testCreateEmployee_Success() throws Exception {

        String token = getValidToken();

        EmployeeRequest request =
                new EmployeeRequest(
                        "Mario",
                        "Rossi",
                        LocalDate.of(1990, 1, 1),
                        "Male",
                        "Italian",
                        "Milano",
                        "+393312345678",
                        "mario.rossi@company.com",
                        "Password123!",
                        "mario.rossi.personal@gmail.com",
                        "Via Roma 10",
                        "Milano",
                        "Italy",
                        "20100",
                        "Lombardia",
                        "Milano HQ",
                        RoleName.ROLE_EMPLOYEE
                );


        mockMvc.perform(
                        post("/api/v1/employee")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testCreateEmployee_ValidationFailure() throws Exception {

        String token = getValidToken();


        EmployeeRequest request =
                new EmployeeRequest(
                        "Mario",
                        "Rossi",
                        LocalDate.of(1990, 1, 1),
                        "Male",
                        "Italian",
                        "Milano",
                        "+393312345678",
                        "email-non-valida",
                        "Password123!",
                        "personal@gmail.com",
                        "Via Roma 10",
                        "Milano",
                        "Italy",
                        "20100",
                        "Lombardia",
                        "Milano HQ",
                        RoleName.ROLE_EMPLOYEE
                );


        mockMvc.perform(
                        post("/api/v1/employee")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testGetEmployee_NotFound() throws Exception {

        String token = getValidToken();


        mockMvc.perform(
                        get("/api/v1/employee/99999")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllEmployees_ReturnsPaginatedList() throws Exception {

        String token = getValidToken();

        EmployeeRequest employee1 =
                createRequest("employee1@test.com");

        EmployeeRequest employee2 =
                createRequest("employee2@test.com");

        mockMvc.perform(
                        post("/api/v1/employee")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(employee1)
                                )
                )
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/v1/employee")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(employee2)
                                )
                )
                .andExpect(status().isCreated());
        mockMvc.perform(
                        get("/api/v1/employee")
                                .param("page", "0")
                                .param("size", "10")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    void testDeleteEmployee_NoContent() throws Exception {

        String token = getValidToken();

        EmployeeRequest request =
                createRequest("delete@test.com");
        mockMvc.perform(
                        post("/api/v1/employee")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isCreated());
        Employee employee =
                employeeRepository
                        .findByEmail("delete@test.com")
                        .orElseThrow();
        mockMvc.perform(
                        delete(
                                "/api/v1/employee/delete/"
                                        + employee.getId()
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isNoContent());
        mockMvc.perform(
                        get(
                                "/api/v1/employee/"
                                        + employee.getId()
                        )
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isNotFound());
    }

    private String getValidToken() throws Exception {

        LoginRequest loginRequest = new LoginRequest(
                "admin@test.com",
                "password"
        );

        String response =
                mockMvc.perform(
                                post("/api/v1/auth/login")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(loginRequest))
                        )
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();


        JsonNode json = objectMapper.readTree(response);

        return json.get("token").asText();
    }

    private EmployeeRequest createRequest(String email) {

        return new EmployeeRequest(
                "Mario",
                "Rossi",
                LocalDate.of(1990, 1, 1),
                "Male",
                "Italian",
                "Milano",
                "+393312345678",
                email,
                "Password123!",
                "personal@gmail.com",
                "Via Roma 10",
                "Milano",
                "Italy",
                "20100",
                "Lombardia",
                "Milano HQ",
                RoleName.ROLE_EMPLOYEE
        );
    }
}
