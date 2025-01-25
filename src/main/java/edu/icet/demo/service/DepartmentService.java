package edu.icet.demo.service;

import edu.icet.demo.dto.Department;
import edu.icet.demo.dto.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {
    Department addDepartment(Department department);

    PaginatedResponse<Department> getAllWithPagination(Pageable pageable, String searchTerm);

    void deleteById(Long id);
}