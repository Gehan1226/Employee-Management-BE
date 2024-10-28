package edu.icet.demo.controller;

import edu.icet.demo.dto.Employee;
import edu.icet.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@CrossOrigin
public class EmployeeController {

    final EmployeeService employeeService;

    @PostMapping("/add-employee")
    public Employee addEmployee(@RequestBody Employee employee, @RequestParam Long roleId, @RequestParam Long departmentId){
        return employeeService.addEmployee(employee, roleId, departmentId);
    }

}
