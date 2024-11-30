package edu.icet.demo.dto;

import lombok.*;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeSkill {
    private Long id;
    private Employee employee;
    private Skill skill;
    private String proficiencyLevel;
    private Date acquisitionDate;
}
