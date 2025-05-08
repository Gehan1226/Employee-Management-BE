package edu.icet.demo.dto.leave;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LocalDateTime appliedOn;
    private Long leaveTypeId;
    private Long employeeId;
}
