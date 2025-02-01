package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.Employee;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.entity.EmployeeEntity;
import edu.icet.demo.exception.DataMisMatchException;
import edu.icet.demo.exception.DataNotFoundException;
import edu.icet.demo.exception.DeletionException;
import edu.icet.demo.exception.MissingAttributeException;
import edu.icet.demo.repository.DepartmentRepository;
import edu.icet.demo.repository.EmployeeRepository;
import edu.icet.demo.repository.RoleRepository;
import edu.icet.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final ObjectMapper mapper;

    @Override
    public Employee addEmployee(Employee employee) {
        validateRoleAndDepartment(employee);
        return mapper.convertValue(
                employeeRepository.save(mapper.convertValue(employee, EmployeeEntity.class)), Employee.class);
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employeeList = new ArrayList<>();

        employeeRepository.findAll().forEach(employeeEntity ->
                employeeList.add(new ObjectMapper().convertValue(employeeEntity, Employee.class)
                ));
        return employeeList;
    }

    @Override
    public void deleteEmployee(String email) {
        if (!employeeRepository.existsByEmail(email)) {
            throw new DataNotFoundException("Employee with email '%s' not found".formatted(email));
        }
        try {
            employeeRepository.deleteByEmail(email);
        } catch (Exception e) {
            throw new DeletionException("Failed to delete employee with email '%s'.".formatted(email));
        }
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        if (!employeeRepository.existsByEmail(employee.getEmail())) {
            throw new DataNotFoundException("Employee with email '%s' not found".formatted(employee.getEmail()));
        }
        if (employee.getAddress().getId() == null) {
            throw new MissingAttributeException("Address ID is missing.");
        }
        validateRoleAndDepartment(employee);
        try {
            return mapper.convertValue(
                    employeeRepository.save(mapper.convertValue(employee, EmployeeEntity.class)), Employee.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataMisMatchException("The current address id is already used!");
        }
    }

    @Override
    public Employee findById(Long id) {
        EmployeeEntity employeeEntity = employeeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Employee with ID %d not found.".formatted(id)));
        return mapper.convertValue(employeeEntity, Employee.class);
    }

    @Override
    public Employee findByFirstName(String firstName) {
        EmployeeEntity employeeEntity = employeeRepository.findByFirstName(firstName);
        if (employeeEntity == null) {
            throw new DataNotFoundException("Employee with first name '%s' not found.".formatted(firstName));
        }
        return mapper.convertValue(employeeEntity, Employee.class);
    }

    @Override
    public PaginatedResponse<Employee> getAllWithPaginated(String searchTerm, Pageable pageable) {
        List<Employee> employeeList = new ArrayList<>();
        Page<EmployeeEntity> response = employeeRepository.findAllWithSearch(searchTerm, pageable);
        response.forEach(employeeEntity ->
                employeeList.add(mapper.convertValue(employeeEntity, Employee.class)));

        return new PaginatedResponse<>(
                HttpStatus.OK.value(),
                employeeList.isEmpty() ? "No roles found!" : "Roles retrieved.",
                employeeList,
                response.getTotalPages(),
                response.getTotalElements(),
                response.getNumber()
        );
    }

    @Override
    public List<Employee> getNonManagers() {
        List<Employee> employeeList = new ArrayList<>();
        employeeRepository.findByIsManagerFalse().forEach(employeeEntity ->
                employeeList.add(mapper.convertValue(employeeEntity, Employee.class)));
        return employeeList;
    }

    public void validateRoleAndDepartment(Employee employee) {
        if (employee.getRole().getId() == null) {
            throw new MissingAttributeException("Role ID must not be null.");
        }
        if (employee.getDepartment().getId() == null) {
            throw new MissingAttributeException("Department ID must not be null.");
        }

        if (!roleRepository.existsById(employee.getRole().getId())) {
            throw new DataNotFoundException(
                    "Role with ID %d not found.".formatted(employee.getRole().getId()));
        }

        if (!departmentRepository.existsById(employee.getDepartment().getId())) {
            throw new DataNotFoundException(
                    "Department with ID %d not found.".formatted(employee.getDepartment().getId()));
        }
    }

}