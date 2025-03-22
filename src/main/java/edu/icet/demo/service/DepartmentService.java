package edu.icet.demo.service;

import edu.icet.demo.dto.department.DepartmentCreateRequest;
import edu.icet.demo.dto.department.DepartmentResponse;
import edu.icet.demo.dto.department.DepartmentNameAndEmployeeCount;
import edu.icet.demo.dto.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {
    void addDepartment(DepartmentCreateRequest department);

    PaginatedResponse<DepartmentResponse> getAllWithPagination(Pageable pageable, String searchTerm);

    void deleteById(Long id);

    List<DepartmentNameAndEmployeeCount> getDepartmentNameWithEmployeeCount();

    void updateDepartment(Long id, DepartmentCreateRequest department);

    List<DepartmentResponse> getAll();
}