package edu.icet.demo.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {

    private Long id;

    @NotBlank(message = "Department name is required.")
    private String name;

    @NotBlank(message = "Department responsibility is required.")
    private String responsibility;

    private Employee manager;

    private Integer employeeCount;

    private Set<Employee> employeeList;

    private Set<Role> roleList;
}