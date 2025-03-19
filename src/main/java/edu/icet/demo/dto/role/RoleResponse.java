package edu.icet.demo.dto.role;

import edu.icet.demo.dto.department.DepartmentResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
    private Long id;
    private String name;
    private String description;
    private Integer employeeCount;
    private DepartmentResponse department;
}