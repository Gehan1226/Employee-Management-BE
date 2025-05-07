package edu.icet.demo.controller;

import edu.icet.demo.dto.manager.ManagerResponse;
import edu.icet.demo.dto.response.SuccessResponseWithData;
import edu.icet.demo.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/managers")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("/by-employee-id/{employeeId}")
    public SuccessResponseWithData<ManagerResponse> getByEmployeeId(@PathVariable Long employeeId) {
        ManagerResponse response = managerService.getByEmployeeId(employeeId);
        return SuccessResponseWithData.<ManagerResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Manager retrieved successfully!")
                .data(response)
                .build();
    }
}
