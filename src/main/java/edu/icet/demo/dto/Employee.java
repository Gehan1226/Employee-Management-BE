package edu.icet.demo.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Date dob;
    private String phoneNumber;
    private String gender;
    private Department department;
    private Role role;
    private Address address;
}