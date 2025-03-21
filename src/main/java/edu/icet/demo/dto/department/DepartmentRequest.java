package edu.icet.demo.dto.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
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
