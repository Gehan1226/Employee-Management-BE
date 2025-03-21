package edu.icet.demo.dto.department;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponse {
    private Long id;
    private String name;
    private String responsibility;
    private ManagerResponse manager;
    private Integer employeeCount;
}