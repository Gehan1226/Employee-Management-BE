package edu.icet.demo.dto.department;

import edu.icet.demo.dto.records.ManagerID;
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

    @NotBlank(message = "Department name is required.")
    private String responsibility;

    @NotNull(message = "Manager is required.")
    private ManagerID manager;
}
