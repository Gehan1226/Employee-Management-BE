package edu.icet.demo.dto.leave;

import edu.icet.demo.dto.employee.EmployeeResponse;
import edu.icet.demo.dto.enums.LeaveStatus;
import edu.icet.demo.entity.LeaveTypeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveResponse {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LocalDateTime appliedOn;
    private LeaveStatus status;
    private LeaveTypeReponse leaveType;
    private EmployeeResponse employee;
}
