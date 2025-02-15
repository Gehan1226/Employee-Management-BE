package edu.icet.demo.service;


import edu.icet.demo.dto.Attendance;
import edu.icet.demo.dto.SaveAttendanceDTO;

public interface AttendanceService {
    void markAttendance(SaveAttendanceDTO attendance);

    Attendance getAttendanceById(Long id);
}
