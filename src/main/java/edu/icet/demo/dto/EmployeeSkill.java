package edu.icet.demo.dto;

import edu.icet.demo.dto.employee.EmployeeRequest;
import lombok.*;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSkill {
    private Long id;
    private EmployeeRequest employeeRequest;
    private Skill skill;
    private String proficiencyLevel;
    private Date acquisitionDate;
}
