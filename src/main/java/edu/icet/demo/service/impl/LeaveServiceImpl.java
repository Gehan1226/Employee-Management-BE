package edu.icet.demo.service.impl;

import edu.icet.demo.dto.enums.LeaveStatus;
import edu.icet.demo.dto.leave.LeaveApprovedRequest;
import edu.icet.demo.dto.leave.LeaveRequest;
import edu.icet.demo.dto.leave.LeaveResponse;
import edu.icet.demo.entity.*;
import edu.icet.demo.exception.DataIntegrityException;
import edu.icet.demo.exception.DataNotFoundException;
import edu.icet.demo.exception.UnexpectedException;
import edu.icet.demo.repository.*;
import edu.icet.demo.service.LeaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveServiceImpl implements LeaveService {

    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveApprovalRepository leaveApprovalRepository;
    private final ModelMapper modelMapper;

    @Override
    public void applyLeave(LeaveRequest leaveRequest) {
        EmployeeEntity employee = employeeRepository.findById(leaveRequest.getEmployeeId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Employee not found with ID: " + leaveRequest.getEmployeeId()));

        LeaveTypeEntity leaveType = leaveTypeRepository.findById(leaveRequest.getLeaveTypeId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Leave type not found with ID: " + leaveRequest.getLeaveTypeId()));

        LeaveBalanceEntity leaveBalance = leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeId(employee.getId(), leaveType.getId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Leave balance not found with employee ID: " + employee.getId() +
                                " and leave type ID: " + leaveType.getId()
                ));

        if (leaveBalance.getUsedDays() >= leaveBalance.getTotalDays()) {
            throw new DataIntegrityException("Leave balance is already full.");
        }

        try {
            LeaveRequestEntity entity = modelMapper.map(leaveRequest, LeaveRequestEntity.class);
            entity.setId(null);
            entity.setEmployee(employee);
            entity.setLeaveType(leaveType);
            entity.setStatus(LeaveStatus.PENDING);

            leaveRequestRepository.save(entity);
        } catch (Exception e) {
            log.error("Error applying leave: {}", e.getMessage(), e);
            throw new UnexpectedException("An unexpected error occurred while applying leave");
        }
    }

    @Override
    public void cancelLeave(Long id) {
        LeaveRequestEntity entity = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        "Leave request not found with ID: " + id));
        try {
            leaveRequestRepository.delete(entity);
        } catch (Exception e) {
            throw new UnexpectedException("An unexpected error occurred while canceling leave");
        }
    }

    @Override
    public List<LeaveResponse> getLeavesByEmployeeId(Long id, LeaveStatus status) {
        try {
            List<LeaveRequestEntity> entities = leaveRequestRepository.findByEmployeeIdAndStatus(id, status);
            return entities.stream().map(entity -> modelMapper.map(entity, LeaveResponse.class)).toList();
        } catch (Exception e) {
            throw new UnexpectedException("An unexpected error occurred while retrieving leaves");
        }
    }

    @Override
    public List<LeaveResponse> getLeavesByDepartmentId(Long id, LeaveStatus status) {
        try {
            List<LeaveRequestEntity> leaveEntities = leaveRequestRepository.findByDepartmentId(id, status);

            return leaveEntities.stream()
                    .map(entity -> modelMapper.map(entity, LeaveResponse.class))
                    .toList();
        } catch (Exception e) {
            log.error("Error getting leaves by department ID: {}", id, e);
            throw new UnexpectedException("An unexpected error occurred while fetching leaves for department ID: " + id);
        }
    }

    @Override
    @Transactional
    public void approveLeave(LeaveApprovedRequest leaveApprovedRequest) {
        LeaveRequestEntity leaveRequestEntity = leaveRequestRepository.findById(leaveApprovedRequest.getLeaveRequestId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Leave request not found with ID: " + leaveApprovedRequest.getLeaveRequestId()
                ));

        EmployeeEntity employeeEntity = employeeRepository.findById(leaveApprovedRequest.getApprovedBYEmployeeId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Employee not found with ID: " + leaveApprovedRequest.getApprovedBYEmployeeId()
                ));

        try {
            LeaveApprovalEntity map = modelMapper.map(leaveApprovedRequest, LeaveApprovalEntity.class);
            map.setId(null);
            map.setLeaveRequestEntity(leaveRequestEntity);
            map.setApprovedBy(employeeEntity);
            leaveApprovalRepository.save(map);
            leaveRequestEntity.setStatus(LeaveStatus.APPROVED);
            leaveRequestRepository.save(leaveRequestEntity);
        } catch (Exception e) {
            throw new UnexpectedException("An unexpected error occurred while approving leave");
        }
    }
}
