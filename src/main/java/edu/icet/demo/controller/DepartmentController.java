package edu.icet.demo.controller;

import edu.icet.demo.dto.operationDTOS.DepartmentOperationDTO;
import edu.icet.demo.dto.Department;
import edu.icet.demo.dto.DepartmentNameAndEmployeeCountDTO;
import edu.icet.demo.dto.response.PaginatedResponse;
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

    @PostMapping("/add")
    public SuccessResponseWithData<DepartmentOperationDTO> addDepartment(
            @Valid @RequestBody DepartmentOperationDTO department,
            BindingResult result) {
        DepartmentOperationDTO addedDepartment = departmentService.addDepartment(department);
        return SuccessResponseWithData.<DepartmentOperationDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("Department added successfully.")
                .data(addedDepartment)
                .build();
    }

    @GetMapping("/get-all-paginated")
    public PaginatedResponse<Department> getAll(
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
    public SuccessResponseWithData<List<DepartmentNameAndEmployeeCountDTO>> getDepartmentNameWithEmployeeCount() {
        return SuccessResponseWithData.<List<DepartmentNameAndEmployeeCountDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Department name and employee count retrieved.")
                .data(departmentService.getDepartmentNameWithEmployeeCount())
                .build();
    }

    @DeleteMapping("/delete")
    public SuccessResponseWithData<String> deleteDepartment(@RequestParam Long id) {
        departmentService.deleteById(id);
        return SuccessResponseWithData.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Department deleted.")
                .build();
    }

    @PutMapping("/update/{id}")
    public SuccessResponseWithData<DepartmentOperationDTO> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentOperationDTO department,
            BindingResult result) {

        DepartmentOperationDTO updatedDepartment = departmentService.updateDepartment(id, department);
        return SuccessResponseWithData.<DepartmentOperationDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Department updated successfully.")
                .data(updatedDepartment)
                .build();
    }


}