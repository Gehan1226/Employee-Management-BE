package edu.icet.demo.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateRequest {
    private String taskDescription;
    private LocalDate assignedDate;
    private LocalTime assignedTime;
    private LocalDate dueDate;
    private LocalTime dueTime;
    private String status;
    private Set<Long> employeeIdList;
}
