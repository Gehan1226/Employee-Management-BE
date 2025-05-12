package edu.icet.demo.service;

import edu.icet.demo.dto.enums.LeaveStatus;
import edu.icet.demo.dto.leave.LeaveApprovedRequest;
import edu.icet.demo.dto.leave.LeaveRequest;
import edu.icet.demo.dto.leave.LeaveResponse;

import java.util.List;

public interface LeaveService {

    void applyLeave(LeaveRequest leaveRequest);

    void cancelLeave(Long id);

    List<LeaveResponse> getLeavesByEmployeeId(Long id, LeaveStatus status);

    List<LeaveResponse> getLeavesByDepartmentId(Long id, LeaveStatus status);

    void approveLeave(LeaveApprovedRequest leaveApprovedRequest);
}
