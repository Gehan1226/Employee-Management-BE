package edu.icet.demo.dto.department;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ManagerResponse {
    private Long id;
    private ManagerEmployeeResponse employee;
}
