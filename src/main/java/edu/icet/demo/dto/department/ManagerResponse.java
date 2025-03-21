package edu.icet.demo.dto.department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ManagerResponse {
    private Long id;
    private ManagerEmployeeResponse employee;
}
