package edu.icet.demo.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {

    private Long id;

    @NotBlank(message = "Department name is required.")
    private String name;

    @NotBlank(message = "Department description is required.")
    private String description;

    private Set<Employee> employeeList;

    private Set<Role> roleList;
}