package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.attendance.AttendanceResponse;
import edu.icet.demo.dto.attendance.AttendanceRequest;
import edu.icet.demo.entity.AttendanceEntity;
import edu.icet.demo.exception.DataDuplicateException;
import edu.icet.demo.exception.DataIntegrityException;
import edu.icet.demo.exception.DataNotFoundException;
import edu.icet.demo.exception.UnexpectedException;
import edu.icet.demo.repository.AttendanceRepository;
import edu.icet.demo.repository.EmployeeRepository;
import edu.icet.demo.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final ObjectMapper mapper;

    @Override
    public void markAttendance(AttendanceRequest attendance) {
        if (attendanceRepository.existsByEmployeeIdAndDate(attendance.getEmployeeId(), attendance.getDate())) {
            throw new DataDuplicateException("Attendance for today is already marked for this employee.");
        }
        if (!employeeRepository.existsById(attendance.getEmployeeId())) {
            throw new DataNotFoundException("Employee with ID " + attendance.getEmployeeId() + " not found.");
        }
        try {
            AttendanceEntity attendanceEntity = mapper.convertValue(attendance, AttendanceEntity.class);
            attendanceRepository.save(attendanceEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(
                    "Database constraint violation. Please check that all provided values are valid and unique.");
        } catch (Exception ex) {
            throw new UnexpectedException("An unexpected error occurred while saving the attendance");
        }
    }

    @Override
    public AttendanceResponse getAttendanceByEmployeeId(Long employeeId, LocalDate date) {
        try{
            AttendanceEntity attendanceEntity = attendanceRepository.findByEmployeeIdAndDate(employeeId, date);
            return mapper.convertValue(attendanceEntity, AttendanceResponse.class);
        } catch (Exception ex) {
            throw new DataNotFoundException("Attendance for employee with ID " + employeeId + " not found.");
        }

    }
}
