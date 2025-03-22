package edu.icet.demo.dto.department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ManagerResponse {
    private Long id;
    private ManagerEmployeeResponse employee;
}
