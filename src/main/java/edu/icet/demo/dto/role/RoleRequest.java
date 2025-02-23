package edu.icet.demo.dto.role;

import edu.icet.demo.dto.records.DepartmentID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {
    @NotBlank(message = "Role name is required.")
    private String name;

    @NotBlank(message = "Role description is required.")
    private String description;

    @NotNull(message = "department id is required.")
    private DepartmentID department;
}
