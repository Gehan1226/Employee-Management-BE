package edu.icet.demo.service;

import edu.icet.demo.dto.Employee;
import edu.icet.demo.dto.auth.UserDTO;

import java.util.List;

public interface EmployeeService {
    Employee addEmployee(Employee employee);
    List<Employee> getAll();

    void deleteEmployee(String email);

    Employee updateEmployee(Employee employee);

    Employee findById(Long id);

    Employee findByFirstName(String firstName);

}
