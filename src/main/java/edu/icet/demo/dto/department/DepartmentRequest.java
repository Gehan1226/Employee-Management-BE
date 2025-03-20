package edu.icet.demo.dto.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequest {

    @NotBlank(message = "Department name is required.")
    private String name;

    @NotBlank(message = "Department responsibility is required.")
    private String responsibility;

    @NotNull(message = "employee id is required.")
    private Long employeeId;
}
