package edu.icet.demo.dto;


import edu.icet.demo.dto.department.DepartmentResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    private Long id;

    @NotBlank(message = "Role name is required.")
    private String name;

    @NotBlank(message = "Role description is required.")
    private String description;

    private Integer employeeCount;

    private List<Employee> employeeList;

    private DepartmentResponse departmentResponse;
}