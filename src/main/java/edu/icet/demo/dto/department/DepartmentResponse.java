package edu.icet.demo.dto.department;

import edu.icet.demo.dto.employee.EmployeeRequest;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponse {

    private Long id;
    private String name;
    private String responsibility;
    private EmployeeRequest manager;
    private Integer employeeCount;
}