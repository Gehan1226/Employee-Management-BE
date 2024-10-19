package edu.icet.demo.service;

import edu.icet.demo.dto.Department;

import java.util.List;

public interface DepartmentService {
    Department addDepartment(Department department);

    List<Department> getAll();

    void deleteById(Long id);
}