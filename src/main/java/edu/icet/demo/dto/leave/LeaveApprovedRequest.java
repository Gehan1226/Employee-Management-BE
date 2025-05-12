package edu.icet.demo.dto.leave;

import edu.icet.demo.dto.enums.LeaveStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveApprovedRequest {
    private LocalDateTime approvedOn;
    private String comments;

    @Enumerated(EnumType.STRING)
    private LeaveStatus status;

    private Long leaveRequestId;

    private Long approvedBYEmployeeId;
}
