package edu.icet.demo.dto.task;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateRequest {

    @NotBlank(message = "Task description cannot be empty")
    @Size(max = 255, message = "Task description cannot exceed 255 characters")
    private String taskDescription;

    @NotNull(message = "Assigned date cannot be null")
    private LocalDate assignedDate;

    @NotNull(message = "Assigned time cannot be null")
    private LocalTime assignedTime;

    @NotNull(message = "Due date cannot be null")
    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDate dueDate;

    @NotNull(message = "Due time cannot be null")
    private LocalTime dueTime;

    @NotBlank(message = "Status cannot be empty")
    @Pattern(regexp = "PENDING|IN_PROGRESS|COMPLETED",
            message = "Invalid status. Allowed values: PENDING, IN_PROGRESS, COMPLETED")
    private String status;

    @NotNull(message = "Manager ID cannot be null")
    @Positive(message = "Manager ID must be a positive number")
    private Long managerId;

    private Set<Long> employeeIdList;
}