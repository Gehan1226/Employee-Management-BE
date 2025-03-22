package edu.icet.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "role")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer employeeCount;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = true) // Allow null values
    private DepartmentEntity department;
}