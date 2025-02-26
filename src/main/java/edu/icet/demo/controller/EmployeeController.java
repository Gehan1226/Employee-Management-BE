package edu.icet.demo.controller;

import edu.icet.demo.dto.Employee;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.dto.response.SuccessResponseWithData;
import edu.icet.demo.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
@CrossOrigin
@Validated
public class EmployeeController {

    final EmployeeService employeeService;

    @PostMapping()
    public SuccessResponseWithData<Employee> addEmployee(@Valid @RequestBody Employee employee, BindingResult result){
        Employee createdEmployee = employeeService.addEmployee(employee);
        return SuccessResponseWithData.<Employee>builder()
                .status(HttpStatus.CREATED.value())
                .message("Employee created successfully.")
                .data(createdEmployee)
                .build();
    }

    @PatchMapping()
    public SuccessResponseWithData<Employee> updateEmployee(@RequestBody Employee employee, BindingResult result){
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        return SuccessResponseWithData.<Employee>builder()
                .status(HttpStatus.OK.value())
                .message("Employee updated successfully.")
                .data(updatedEmployee)
                .build();
    }

    @GetMapping("/employees")
    public SuccessResponseWithData<List<Employee>> getEmployees(){
        List<Employee> employeeList = employeeService.getAll();
        String message = employeeList.isEmpty() ? "Employee List is empty!" : "Employee list retrieved.";
        return SuccessResponseWithData.<List<Employee>>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(employeeList)
                .build();
    }

    @GetMapping("/paginated-employees")
    public PaginatedResponse<Employee> getEmployeesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm){

        Pageable pageable = PageRequest.of(page, size);
        return employeeService.getAllWithPaginated(searchTerm, pageable);
    }

    @DeleteMapping()
    public SuccessResponseWithData<String> deleteEmployees(@RequestParam String email){
        employeeService.deleteEmployee(email);
        return SuccessResponseWithData.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Employee deleted!")
                .build();
    }

    @GetMapping("/non-managers")
    public SuccessResponseWithData<List<Employee>> getNonManagers(){
        List<Employee> employeeList = employeeService.getNonManagers();
        String message = employeeList.isEmpty() ? "No non-managers found!" : "Non-managers retrieved.";
        return SuccessResponseWithData.<List<Employee>>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(employeeList)
                .build();
    }
}
