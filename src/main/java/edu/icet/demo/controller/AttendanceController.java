package edu.icet.demo.controller;

import edu.icet.demo.dto.Attendance;
import edu.icet.demo.dto.SaveAttendanceDTO;
import edu.icet.demo.dto.response.SuccessResponse;
import edu.icet.demo.dto.response.SuccessResponseWithData;
import edu.icet.demo.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
@CrossOrigin
@Validated
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping()
    public SuccessResponse markAttendance(@RequestBody SaveAttendanceDTO attendance) {
        attendanceService.markAttendance(attendance);
        return SuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Attendance marked successfully!").build();
    }

    @GetMapping("/{id}")
    public SuccessResponseWithData<Attendance> getAttendanceById(@PathVariable Long id) {
        return SuccessResponseWithData.<Attendance>builder()
                .status(HttpStatus.OK.value())
                .message("Attendance retrieved.")
                .data(attendanceService.getAttendanceById(id))
                .build();
    }
}
