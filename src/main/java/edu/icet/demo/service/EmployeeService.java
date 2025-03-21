package edu.icet.demo.service;

import edu.icet.demo.dto.employee.EmployeeCreateRequest;
import edu.icet.demo.dto.employee.EmployeeResponse;
import edu.icet.demo.dto.employee.EmployeeUpdateRequest;
import edu.icet.demo.dto.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    void addEmployee(EmployeeCreateRequest employeeCreateRequest);

    List<EmployeeResponse> getAll();

    void deleteEmployeeBYId(Long id);

    void updateEmployee(Long id, EmployeeUpdateRequest employeeRequest);

    EmployeeCreateRequest findById(Long id);

    EmployeeCreateRequest findByFirstName(String firstName);

    PaginatedResponse<EmployeeResponse> getAllWithPaginated(String searchTerm, Pageable pageable);

    List<EmployeeResponse> getNonManagers();

    List<EmployeeResponse> getEmployeesByDepartmentId(Long id);
}
