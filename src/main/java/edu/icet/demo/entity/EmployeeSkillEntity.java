package edu.icet.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name = "employee_skill")
public class EmployeeSkillEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employeeEntity;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private SkillEntity skillEntity;

    private String proficiencyLevel;
    private Date acquisitionDate;
}
