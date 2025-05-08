package edu.icet.demo.controller;

import edu.icet.demo.dto.enums.LeaveStatus;
import edu.icet.demo.dto.leave.LeaveRequest;
import edu.icet.demo.dto.leave.LeaveResponse;
import edu.icet.demo.dto.response.SuccessResponse;
import edu.icet.demo.dto.response.SuccessResponseWithData;
import edu.icet.demo.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leaves")
@RequiredArgsConstructor
@Validated
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping()
    public SuccessResponse applyLeave(@RequestBody LeaveRequest leaveRequest) {
        leaveService.applyLeave(leaveRequest);
        return SuccessResponse.builder().status(HttpStatus.OK.value()).message("Leave applied successfully!").build();
    }

    @DeleteMapping("/{id}")
    public SuccessResponse cancelLeave(@PathVariable Long id) {
        leaveService.cancelLeave(id);
        return SuccessResponse.builder().status(HttpStatus.OK.value()).message("Leave cancelled successfully!").build();
    }

    @GetMapping("by-employee-id/{id}")
    public SuccessResponseWithData<List<LeaveResponse>> getLeavesByEmployeeId(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "PENDING") LeaveStatus status) {
        List<LeaveResponse> leaves = leaveService.getLeavesByEmployeeId(id, status);
        return SuccessResponseWithData.<List<LeaveResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Leaves fetched successfully!")
                .data(leaves)
                .build();
    }

    @GetMapping("by-department-id/{id}")
    public SuccessResponseWithData<List<LeaveResponse>> getLeavesByDepartmentId(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "PENDING") LeaveStatus status) {
        List<LeaveResponse> leaves = leaveService.getLeavesByDepartmentId(id, status);
        return SuccessResponseWithData.<List<LeaveResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Leaves fetched successfully!")
                .data(leaves)
                .build();
    }


}
