package edu.icet.demo.controller;

import edu.icet.demo.dto.department.DepartmentCreateRequest;
import edu.icet.demo.dto.department.DepartmentResponse;
import edu.icet.demo.dto.department.DepartmentNameAndEmployeeCount;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.dto.response.SuccessResponse;
import edu.icet.demo.dto.response.SuccessResponseWithData;
import edu.icet.demo.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/department")
@RequiredArgsConstructor
@CrossOrigin
@Validated
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping()
    public SuccessResponse addDepartment(
            @Valid @RequestBody DepartmentCreateRequest department,
            BindingResult result) {
        departmentService.addDepartment(department);
        return SuccessResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Department created successfully.")
                .build();
    }

    @GetMapping("/get-all-paginated")
    public PaginatedResponse<DepartmentResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "employeeCount") String sortBy) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return departmentService.getAllWithPagination(pageable, searchTerm);
    }

    @GetMapping("/names-and-employee-counts")
    public SuccessResponseWithData<List<DepartmentNameAndEmployeeCount>> getDepartmentNameWithEmployeeCount() {
        return SuccessResponseWithData.<List<DepartmentNameAndEmployeeCount>>builder()
                .status(HttpStatus.OK.value())
                .message("Department name and employee count retrieved.")
                .data(departmentService.getDepartmentNameWithEmployeeCount())
                .build();
    }

    @DeleteMapping("/{id}")
    public SuccessResponse deleteDepartment(@PathVariable Long id) {
        departmentService.deleteById(id);
        return SuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Department deleted.")
                .build();
    }

    @PatchMapping("/{id}")
    public SuccessResponse updateDepartment(
            @PathVariable Long id,
            @RequestBody DepartmentCreateRequest department,
            BindingResult result) {

        departmentService.updateDepartment(id, department);
        return SuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Department updated successfully.")
                .build();
    }

    @GetMapping("/get-all")
    public SuccessResponseWithData<List<DepartmentResponse>> getAll() {
        return SuccessResponseWithData.<List<DepartmentResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Departments retrieved.")
                .data(departmentService.getAll())
                .build();
    }
}