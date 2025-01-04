package edu.icet.demo.controller;

import edu.icet.demo.dto.Employee;
import edu.icet.demo.dto.response.SuccessResponse;
import edu.icet.demo.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee") // Added versioning here
@RequiredArgsConstructor
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
    public SuccessResponse<Employee> updateEmployee(@RequestBody Employee employee, BindingResult result){
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        return SuccessResponse.<Employee>builder()
                .status(HttpStatus.OK.value())
                .message("Employee updated successfully.")
                .data(updatedEmployee)
                .build();
    }

    @GetMapping("/get-all")
    public SuccessResponse<List<Employee>> getEmployees(){
        List<Employee> employeeList = employeeService.getAll();
        String message = employeeList.isEmpty() ? "Employee List is empty!" : "Employee list retrieved.";
        return SuccessResponse.<List<Employee>>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(employeeList)
                .build();
    }

    @DeleteMapping("/delete")
    public SuccessResponse<String> deleteEmployees(@RequestParam String email){
        employeeService.deleteEmployee(email);
        return SuccessResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Employee deleted!")
                .build();
    }
}
