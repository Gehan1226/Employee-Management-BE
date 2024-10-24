package edu.icet.demo.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String departmentId;
    private Role role;
}