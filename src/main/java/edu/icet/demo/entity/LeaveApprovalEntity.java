package edu.icet.demo.entity;

import edu.icet.demo.dto.enums.LeaveStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "leave_approvals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApprovalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime approvedOn;
    private String comments;

    @Enumerated(EnumType.STRING)
    private LeaveStatus status;

    @ManyToOne
    @JoinColumn(name = "leave_request_id")
    private LeaveRequestEntity leaveRequestEntity;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private EmployeeEntity approvedBy;
}

