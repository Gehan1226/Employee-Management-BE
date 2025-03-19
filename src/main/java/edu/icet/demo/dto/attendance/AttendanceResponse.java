package edu.icet.demo.dto.attendance;

import edu.icet.demo.dto.employee.EmployeeRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceResponse {
    private Long id;
    private LocalDate date;
    private String status;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private Integer hoursWorked;
    private String shift;
    private EmployeeRequest employeeRequest;
}