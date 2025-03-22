package edu.icet.demo.dto.department;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentCreateRequest {

    @NotBlank(message = "Department name is required.")
    private String name;

    @NotBlank(message = "Department responsibility is required.")
    private String responsibility;

    private Long employeeId;
}
