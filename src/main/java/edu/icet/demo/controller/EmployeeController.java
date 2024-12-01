package edu.icet.demo.controller;

import edu.icet.demo.dto.Employee;
import edu.icet.demo.dto.SuccessResponse;
import edu.icet.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@CrossOrigin
public class EmployeeController {

    final EmployeeService employeeService;

    @PostMapping("/add")
    public SuccessResponse<Employee> addEmployee(@RequestBody Employee employee){
        Employee createdEmployee = employeeService.addEmployee(employee);
        return SuccessResponse.<Employee>builder()
                .status(HttpStatus.CREATED.value())
                .message("Employee created successfully.")
                .data(createdEmployee)
                .build();
    }
}
