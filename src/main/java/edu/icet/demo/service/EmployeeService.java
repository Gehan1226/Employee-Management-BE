package edu.icet.demo.service;

import edu.icet.demo.dto.Employee;

import java.util.List;

public interface EmployeeService {
    Employee addEmployee(Employee employee);
    List<Employee> getAll();

    void deleteEmployee(Long id);

    Employee updateEmployee(Employee employee);

    Employee findById(Long id);

    Employee findByFirstName(String firstName);
}
