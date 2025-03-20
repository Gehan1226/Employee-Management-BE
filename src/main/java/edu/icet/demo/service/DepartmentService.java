package edu.icet.demo.service;

import edu.icet.demo.dto.department.DepartmentRequest;
import edu.icet.demo.dto.department.DepartmentResponse;
import edu.icet.demo.dto.DepartmentNameAndEmployeeCountDTO;
import edu.icet.demo.dto.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {
    void addDepartment(DepartmentRequest department);

    PaginatedResponse<DepartmentResponse> getAllWithPagination(Pageable pageable, String searchTerm);

    void deleteById(Long id);

    List<DepartmentNameAndEmployeeCountDTO> getDepartmentNameWithEmployeeCount();

    DepartmentRequest updateDepartment(Long id, DepartmentRequest department);

    List<DepartmentResponse> getAll();
}