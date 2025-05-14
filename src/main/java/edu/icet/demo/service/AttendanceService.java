package edu.icet.demo.service;


import edu.icet.demo.dto.attendance.AttendanceResponse;
import edu.icet.demo.dto.attendance.AttendanceRequest;

import java.time.LocalDate;

public interface AttendanceService {
    void markAttendance(AttendanceRequest attendance);
    AttendanceResponse getAttendanceByEmployeeId(Long employeeId, LocalDate date);
}
