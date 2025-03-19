package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.employee.EmployeeRequest;
import edu.icet.demo.dto.employee.EmployeeResponse;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.entity.DepartmentEntity;
import edu.icet.demo.entity.EmployeeEntity;
import edu.icet.demo.entity.RoleEntity;
import edu.icet.demo.exception.*;
import edu.icet.demo.repository.DepartmentRepository;
import edu.icet.demo.repository.EmployeeRepository;
import edu.icet.demo.repository.RoleRepository;
import edu.icet.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final ObjectMapper mapper;

    private static final String ERROR_MESSAGE = "An unexpected error occurred while fetching employees.";

    @Override
    public void addEmployee(EmployeeRequest employeeRequest) {
        if (employeeRepository.existsByEmail(employeeRequest.getEmail())) {
            throw new DataDuplicateException(
                    "An employee with the email '" + employeeRequest.getEmail() + "' already exists.");
        }
        if (!departmentRepository.existsById(employeeRequest.getDepartmentId())) {
            throw new DataNotFoundException("Department with ID %d not found."
                    .formatted(employeeRequest.getDepartmentId()));
        }
        if (!roleRepository.existsById(employeeRequest.getRoleId())) {
            throw new DataNotFoundException("Role with ID %d not found."
                    .formatted(employeeRequest.getRoleId()));
        }
        try {
            EmployeeEntity employeeEntity = mapper.convertValue(employeeRequest, EmployeeEntity.class);
            employeeEntity.setDepartment(DepartmentEntity.builder().id(employeeRequest.getDepartmentId()).build());
            employeeEntity.setRole(RoleEntity.builder().id(employeeRequest.getRoleId()).build());
            employeeRepository.save(employeeEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(
                    "A data integrity violation occurred while saving the employee");
        } catch (Exception ex) {
            throw new UnexpectedException("An unexpected error occurred while saving the employee");
        }
    }

    @Override
    public List<EmployeeResponse> getAll() {
        List<EmployeeResponse> employeeRequestList = new ArrayList<>();

        try {
            employeeRepository.findAll().forEach(employeeEntity ->
                    employeeRequestList.add(mapper.convertValue(employeeEntity, EmployeeResponse.class)
                    ));
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e);
            throw new UnexpectedException(ERROR_MESSAGE);
        }
        return employeeRequestList;
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
    public EmployeeRequest updateEmployee(EmployeeRequest employeeRequest) {
        if (!employeeRepository.existsByEmail(employeeRequest.getEmail())) {
            throw new DataNotFoundException("Employee with email '%s' not found".formatted(employeeRequest.getEmail()));
        }
        if (employeeRequest.getAddress().getId() == null) {
            throw new MissingAttributeException("Address ID is missing.");
        }
        try {
            return mapper.convertValue(
                    employeeRepository.save(mapper.convertValue(employeeRequest, EmployeeEntity.class)), EmployeeRequest.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("The current address id is already used!");
        }
    }

    @Override
    public EmployeeRequest findById(Long id) {
        EmployeeEntity employeeEntity = employeeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Employee with ID %d not found.".formatted(id)));
        return mapper.convertValue(employeeEntity, EmployeeRequest.class);
    }

    @Override
    public EmployeeRequest findByFirstName(String firstName) {
        EmployeeEntity employeeEntity = employeeRepository.findByFirstName(firstName);
        if (employeeEntity == null) {
            throw new DataNotFoundException("Employee with first name '%s' not found.".formatted(firstName));
        }
        return mapper.convertValue(employeeEntity, EmployeeRequest.class);
    }

    @Override
    public PaginatedResponse<EmployeeRequest> getAllWithPaginated(String searchTerm, Pageable pageable) {
        try {
            List<EmployeeRequest> employeeRequestList = new ArrayList<>();
            Page<EmployeeEntity> response = employeeRepository.findAllWithSearch(searchTerm, pageable);
            response.forEach(employeeEntity ->
                    employeeRequestList.add(mapper.convertValue(employeeEntity, EmployeeRequest.class)));

            return new PaginatedResponse<>(
                    HttpStatus.OK.value(),
                    employeeRequestList.isEmpty() ? "No roles found!" : "Roles retrieved.",
                    employeeRequestList,
                    response.getTotalPages(),
                    response.getTotalElements(),
                    response.getNumber()
            );
        } catch (Exception e) {
            throw new UnexpectedException(ERROR_MESSAGE);
        }
    }

    @Override
    public List<EmployeeRequest> getNonManagers() {
        try {
            List<EmployeeRequest> employeeRequestList = new ArrayList<>();
//            employeeRepository.findByIsManagerFalse().forEach(employeeEntity ->
//                    employeeList.add(mapper.convertValue(employeeEntity, Employee.class)));
            return employeeRequestList;
        } catch (Exception e) {
            throw new UnexpectedException("An unexpected error occurred while fetching non-managers.");
        }
    }

    @Override
    public List<EmployeeRequest> getEmployeesByDepartmentId(Long id) {
        try {
            if (departmentRepository.existsById(id)) {
                List<EmployeeRequest> employeeRequestList = new ArrayList<>();
                employeeRepository.findByDepartmentId(id).forEach(employeeEntity ->
                        employeeRequestList.add(mapper.convertValue(employeeEntity, EmployeeRequest.class)));
                return employeeRequestList;
            }
        } catch (Exception e) {
            throw new UnexpectedException(ERROR_MESSAGE);
        }
        throw new DataNotFoundException("Department with ID %d not found.".formatted(id));
    }
}