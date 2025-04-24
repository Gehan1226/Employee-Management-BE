package edu.icet.demo.controller;

import edu.icet.demo.dto.employee.EmployeeCreateRequest;
import edu.icet.demo.dto.employee.EmployeeResponse;
import edu.icet.demo.dto.employee.EmployeeUpdateRequest;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.dto.response.SuccessResponse;
import edu.icet.demo.dto.response.SuccessResponseWithData;
import edu.icet.demo.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Validated
public class EmployeeController {

    final EmployeeService employeeService;

    @PostMapping()
    public SuccessResponse addEmployee(
            @Valid @RequestBody EmployeeCreateRequest employeeCreateRequest, BindingResult result) {
        employeeService.addEmployee(employeeCreateRequest);
        return SuccessResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Employee created successfully.").build();
    }

    @PatchMapping("/{id}")
    public SuccessResponse updateEmployee(
            @PathVariable Long id, @Valid @RequestBody EmployeeUpdateRequest employeeRequest, BindingResult result) {

        employeeService.updateEmployee(id, employeeRequest);
        return SuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Employee updated successfully.")
                .build();
    }

    @GetMapping()
    public SuccessResponseWithData<List<EmployeeResponse>> getEmployees() {
        List<EmployeeResponse> employeeRequestList = employeeService.getAll();
        String message = employeeRequestList.isEmpty() ? "Employee List is empty!" : "Employee list retrieved.";
        return SuccessResponseWithData.<List<EmployeeResponse>>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(employeeRequestList)
                .build();
    }

    @GetMapping("/paginated-employees")
    public PaginatedResponse<EmployeeResponse> getEmployeesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm) {

        Pageable pageable = PageRequest.of(page, size);
        return employeeService.getAllWithPaginated(searchTerm, pageable);
    }

    @DeleteMapping("/{id}")
    public SuccessResponse deleteEmployees(@PathVariable Long id) {
        employeeService.deleteEmployeeBYId(id);
        return SuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Employee deleted!")
                .build();
    }

    @GetMapping("/non-managers")
    public SuccessResponseWithData<List<EmployeeResponse>> getNonManagers() {
        List<EmployeeResponse> employeeRequestList = employeeService.getNonManagers();
        String message = employeeRequestList.isEmpty() ? "No non-managers found!" : "Non-managers retrieved.";
        return SuccessResponseWithData.<List<EmployeeResponse>>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(employeeRequestList)
                .build();
    }

    @GetMapping("/by-department/{departmentId}")
    public PaginatedResponse<EmployeeResponse> getEmployeesByDepartmentId(
            @PathVariable Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm) {

        Pageable pageable = PageRequest.of(page, size);
        Page<EmployeeResponse> employeeResponsePage =
                employeeService.getEmployeesByDepartmentId(departmentId, searchTerm, pageable);

        String message = employeeResponsePage.isEmpty()
                ? "No employees found for this department!"
                : "Employees retrieved.";

        return PaginatedResponse.<EmployeeResponse>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(employeeResponsePage.getContent())
                .totalPages(employeeResponsePage.getTotalPages())
                .totalElements(employeeResponsePage.getTotalElements())
                .currentPage(employeeResponsePage.getNumber())
                .build();
    }

    @GetMapping("/by-department/{departmentId}/all")
    public SuccessResponseWithData<List<EmployeeResponse>> getEmployeesByDepartmentId(@PathVariable Long departmentId) {
        List<EmployeeResponse> employeesByDepartmentId = employeeService.getEmployeesByDepartmentId(departmentId);
        String message = employeesByDepartmentId.isEmpty()
                ? "Employees not found for this department!" : "Employees retrieved.";
        return SuccessResponseWithData.<List<EmployeeResponse>>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(employeesByDepartmentId)
                .build();
    }

}
