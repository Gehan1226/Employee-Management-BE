package edu.icet.demo.controller;

import edu.icet.demo.dto.Employee;
import edu.icet.demo.dto.SuccessResponse;
import edu.icet.demo.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@CrossOrigin
@Validated
public class EmployeeController {

    final EmployeeService employeeService;

    @PostMapping("/add")
    public SuccessResponse<Employee>  addEmployee(@Valid @RequestBody Employee employee, BindingResult result){
        Employee createdEmployee = employeeService.addEmployee(employee);

        return SuccessResponse.<Employee>builder()
                .status(HttpStatus.CREATED.value())
                .message("Employee created successfully.")
                .data(createdEmployee)
                .build();
    }

    @PatchMapping("/update")
    public SuccessResponse<Employee> updateEmployee(@Valid @RequestBody Employee employee){
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        return SuccessResponse.<Employee>builder()
                .status(HttpStatus.OK.value())
                .message("Employee updated successfully.")
                .data(updatedEmployee)
                .build();
    }
}
