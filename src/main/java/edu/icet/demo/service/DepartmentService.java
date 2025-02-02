package edu.icet.demo.service;

import edu.icet.demo.dto.Department;
import edu.icet.demo.dto.DepartmentNameAndEmployeeCountDTO;
import edu.icet.demo.dto.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {
    Department addDepartment(Department department);

    PaginatedResponse<Department> getAllWithPagination(Pageable pageable, String searchTerm);

    void deleteById(Long id);

    List<DepartmentNameAndEmployeeCountDTO> getDepartmentNameWithEmployeeCount();
}