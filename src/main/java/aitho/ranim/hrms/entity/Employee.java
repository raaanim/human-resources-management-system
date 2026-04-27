package aitho.ranim.hrms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Table(name = "employees")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String status;
    private String activationToken;
    private LocalDate dateOfBirth;
    private String gender;
    private String nationality;
    private String birthPlace;
    private String phoneNumber;
    private String personalEmail;
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private String province;
    private String workLocation;
}
