package edu.icet.demo.service;


import edu.icet.demo.dto.attendance.AttendanceResponse;
import edu.icet.demo.dto.attendance.AttendanceRequest;

public interface AttendanceService {
    void markAttendance(AttendanceRequest attendance);
    AttendanceResponse getAttendanceByEmployeeId(Long id);
}
