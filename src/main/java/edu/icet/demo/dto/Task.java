package edu.icet.demo.dto;

import edu.icet.demo.dto.employee.EmployeeRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private Long id;
    private String taskDescription;
    private LocalDate assignedDate;
    private LocalTime assignedTime;
    private LocalDate dueDate;
    private LocalTime dueTime;
    private String status;
    private List<EmployeeRequest> employeeRequestList;
}