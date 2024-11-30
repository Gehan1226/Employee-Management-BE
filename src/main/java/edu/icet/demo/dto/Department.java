package edu.icet.demo.dto;

import lombok.*;

import java.util.List;

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
    private List<Employee> employeeList;
}