package edu.icet.demo.service.impl;

import edu.icet.demo.dto.enums.LeaveStatus;
import edu.icet.demo.dto.leave.LeaveRequest;
import edu.icet.demo.dto.leave.LeaveResponse;
import edu.icet.demo.entity.EmployeeEntity;
import edu.icet.demo.entity.LeaveRequestEntity;
import edu.icet.demo.entity.LeaveTypeEntity;
import edu.icet.demo.exception.DataNotFoundException;
import edu.icet.demo.exception.UnexpectedException;
import edu.icet.demo.repository.EmployeeRepository;
import edu.icet.demo.repository.LeaveRequestRepository;
import edu.icet.demo.repository.LeaveTypeRepository;
import edu.icet.demo.service.LeaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveServiceImpl implements LeaveService {

    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final ModelMapper modelMapper;

    @Override
    public void applyLeave(LeaveRequest leaveRequest) {
        EmployeeEntity employee = employeeRepository.findById(leaveRequest.getEmployeeId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Employee not found with ID: " + leaveRequest.getEmployeeId()));

        LeaveTypeEntity leaveType = leaveTypeRepository.findById(leaveRequest.getLeaveTypeId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Leave type not found with ID: " + leaveRequest.getLeaveTypeId()));
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
    public List<LeaveResponse> getLeavesByEmployeeId(Long id) {
        try {
            List<LeaveRequestEntity> entities = leaveRequestRepository.findByEmployeeId(id);
            return entities.stream().map(entity -> modelMapper.map(entity, LeaveResponse.class)).toList();
        } catch (Exception e) {
            throw new UnexpectedException("An unexpected error occurred while retrieving leaves");
        }
    }

    @Override
    public List<LeaveResponse> getLeavesByDepartmentId(Long id) {
        return null;
    }
}
