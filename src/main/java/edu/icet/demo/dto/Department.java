package edu.icet.demo.dto;

import lombok.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Department {
    private Long id;
    private String name;
    private String description;
    private Set<Employee> employeeList;
}