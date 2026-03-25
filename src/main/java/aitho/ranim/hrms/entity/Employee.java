package aitho.ranim.hrms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotNull
    private String firstName;
    @Column(nullable = false)
    @NotNull
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String nationality;
    private String birthPlace;
    private String phoneNumber;
    @Column(unique = true, nullable = false)
    @NotNull
    private String workEmail;
    @Column( nullable = false)
    @NotNull
    private String password;
    private String personalEmail;
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private String province;
    private String workLocation;
    private String status;

}
