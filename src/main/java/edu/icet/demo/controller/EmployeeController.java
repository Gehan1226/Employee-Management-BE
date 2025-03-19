package edu.icet.demo.controller;

import edu.icet.demo.dto.employee.EmployeeRequest;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.dto.response.SuccessResponse;
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
    public SuccessResponse addEmployee(
            @Valid @RequestBody EmployeeRequest employeeRequest, BindingResult result){
        employeeService.addEmployee(employeeRequest);
        return SuccessResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Employee created successfully.").build();
    }

    @PatchMapping()
    public SuccessResponseWithData<EmployeeRequest> updateEmployee(@RequestBody EmployeeRequest employeeRequest, BindingResult result){
        EmployeeRequest updatedEmployeeRequest = employeeService.updateEmployee(employeeRequest);
        return SuccessResponseWithData.<EmployeeRequest>builder()
                .status(HttpStatus.OK.value())
                .message("Employee updated successfully.")
                .data(updatedEmployeeRequest)
                .build();
    }

    @GetMapping("/employees")
    public SuccessResponseWithData<List<EmployeeRequest>> getEmployees(){
        List<EmployeeRequest> employeeRequestList = employeeService.getAll();
        String message = employeeRequestList.isEmpty() ? "Employee List is empty!" : "Employee list retrieved.";
        return SuccessResponseWithData.<List<EmployeeRequest>>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(employeeRequestList)
                .build();
    }

    @GetMapping("/paginated-employees")
    public PaginatedResponse<EmployeeRequest> getEmployeesPaginated(
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
    public SuccessResponseWithData<List<EmployeeRequest>> getNonManagers(){
        List<EmployeeRequest> employeeRequestList = employeeService.getNonManagers();
        String message = employeeRequestList.isEmpty() ? "No non-managers found!" : "Non-managers retrieved.";
        return SuccessResponseWithData.<List<EmployeeRequest>>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(employeeRequestList)
                .build();
    }

    @GetMapping("/by-department/{id}")
    public SuccessResponseWithData<List<EmployeeRequest>> getEmployeesByDepartmentId(@PathVariable Long id){
        List<EmployeeRequest> employeeRequestList = employeeService.getEmployeesByDepartmentId(id);
        String message = employeeRequestList.isEmpty() ? "No employees found for this department!" : "Employees retrieved.";
        return SuccessResponseWithData.<List<EmployeeRequest>>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(employeeRequestList)
                .build();
    }
}
