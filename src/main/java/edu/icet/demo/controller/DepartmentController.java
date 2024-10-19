package edu.icet.demo.controller;

import edu.icet.demo.dto.Department;
import edu.icet.demo.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
@CrossOrigin
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("add-department")
    public Department addDepartment(@RequestBody Department department){
        return departmentService.addDepartment(department);
    }
}