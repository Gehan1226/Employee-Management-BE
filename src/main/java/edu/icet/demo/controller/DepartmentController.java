package edu.icet.demo.controller;

import edu.icet.demo.dto.Department;
import edu.icet.demo.dto.response.SuccessResponse;
import edu.icet.demo.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/department")
@RequiredArgsConstructor
@CrossOrigin
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("add-department")
    public Department addDepartment(@RequestBody Department department){
        return departmentService.addDepartment(department);
    }

    @GetMapping("/get-all")
    public SuccessResponse<List<Department>> getRoles(){
        List<Department> departmentList = departmentService.getAll();
        String message = departmentList.isEmpty() ? "Department List is empty!" : "Department list retrieved.";
        return SuccessResponse.<List<Department>>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(departmentList)
                .build();
    }


}