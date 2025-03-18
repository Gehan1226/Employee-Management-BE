package edu.icet.demo.dto.department;

import edu.icet.demo.dto.Employee;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponse {

    private Long id;
    private String name;
    private String responsibility;
    private Employee manager;
    private Integer employeeCount;
}