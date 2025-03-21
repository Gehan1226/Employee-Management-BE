package edu.icet.demo.service;

import edu.icet.demo.dto.employee.EmployeeRequest;
import edu.icet.demo.dto.employee.EmployeeResponse;
import edu.icet.demo.dto.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    void addEmployee(EmployeeRequest employeeRequest);

    List<EmployeeResponse> getAll();

    void deleteEmployee(String email);

    void updateEmployee(Long id, EmployeeRequest employeeRequest);

    EmployeeRequest findById(Long id);

    EmployeeRequest findByFirstName(String firstName);

    PaginatedResponse<EmployeeResponse> getAllWithPaginated(String searchTerm, Pageable pageable);

    List<EmployeeResponse> getNonManagers();

    List<EmployeeResponse> getEmployeesByDepartmentId(Long id);
}
