package edu.icet.demo.dto.department;

import edu.icet.demo.dto.Employee;
import edu.icet.demo.dto.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponse {

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