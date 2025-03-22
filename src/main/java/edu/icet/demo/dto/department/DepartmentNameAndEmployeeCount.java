package edu.icet.demo.dto.department;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentNameAndEmployeeCount {
    private String name;
    private Integer employeeCount;
}
