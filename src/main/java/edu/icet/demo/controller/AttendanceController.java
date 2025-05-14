package edu.icet.demo.controller;

import edu.icet.demo.dto.attendance.AttendanceResponse;
import edu.icet.demo.dto.attendance.AttendanceRequest;
import edu.icet.demo.dto.response.SuccessResponse;
import edu.icet.demo.dto.response.SuccessResponseWithData;
import edu.icet.demo.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/attendances")
@RequiredArgsConstructor
@Validated
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping()
    public SuccessResponse markAttendance(@RequestBody AttendanceRequest attendance) {
        attendanceService.markAttendance(attendance);
        return SuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Attendance marked successfully!").build();
    }

    @GetMapping("/employee/{employeeId}")
    public SuccessResponseWithData<AttendanceResponse> getAttendanceByEmployeeId(
            @PathVariable Long employeeId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return SuccessResponseWithData.<AttendanceResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Attendance retrieved.")
                .data(attendanceService.getAttendanceByEmployeeId(employeeId, date))
                .build();
    }
}
