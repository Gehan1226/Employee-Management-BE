package edu.icet.demo.service.impl;

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
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper mapper;
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
            EmployeeEntity employeeEntity = mapper.map(employeeRequest, EmployeeEntity.class);
            employeeEntity.setDepartment(DepartmentEntity.builder().id(employeeRequest.getDepartmentId()).build());
            employeeEntity.setRole(RoleEntity.builder().id(employeeRequest.getRoleId()).build());
            employeeRepository.save(employeeEntity);
        } catch (DataIntegrityViolationException ex) {
            log.error(ex.getMessage());
            throw new DataIntegrityException(
                    "A data integrity violation occurred while saving the employee");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new UnexpectedException("An unexpected error occurred while saving the employee");
        }
    }

    @Override
    public List<EmployeeResponse> getAll() {
        List<EmployeeResponse> employeeRequestList = new ArrayList<>();

        try {
            employeeRepository.findAll().forEach(employeeEntity ->
                    employeeRequestList.add(mapper.map(employeeEntity, EmployeeResponse.class)
                    ));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UnexpectedException(ERROR_MESSAGE);
        }
        return employeeRequestList;
    }

    @Override
    @Transactional
    public void deleteEmployee(String email) {
        if (!employeeRepository.existsByEmail(email)) {
            throw new DataNotFoundException("Employee with email '%s' not found".formatted(email));
        }
        try {
            employeeRepository.deleteByEmail(email);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DeletionException("Failed to delete employee with email '%s'.".formatted(email));
        }
    }

    @Override
    public void updateEmployee(Long id, EmployeeRequest employeeRequest) {
        EmployeeEntity employeeEntity = employeeRepository.findById(id).orElseThrow(() -> new DataNotFoundException(
                "Employee with ID %d not found.".formatted(id)
        ));

        if (employeeRequest.getDepartmentId() != null) {
            if (!departmentRepository.existsById(employeeRequest.getDepartmentId())) {
                throw new DataNotFoundException("Department with ID %d not found."
                        .formatted(employeeRequest.getDepartmentId()));
            }
            employeeEntity.setDepartment(DepartmentEntity.builder().id(employeeRequest.getDepartmentId()).build());
        }

        if (employeeRequest.getRoleId() != null) {
            if (!roleRepository.existsById(employeeRequest.getRoleId())) {
                throw new DataNotFoundException("Role with ID %d not found."
                        .formatted(employeeRequest.getRoleId()));
            }
            employeeEntity.setRole(RoleEntity.builder().id(employeeRequest.getRoleId()).build());
        }

        try {
            mapper.map(employeeRequest, employeeEntity);
            employeeRepository.save(employeeEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(
                    "Database constraint violation while updating employee. Please check the provided data.");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UnexpectedException("An unexpected error occurred while updating the employee with ID "
                    + id + ". Please try again later.");
        }
    }

    @Override
    public EmployeeRequest findById(Long id) {
        EmployeeEntity employeeEntity = employeeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Employee with ID %d not found.".formatted(id)));
        return mapper.map(employeeEntity, EmployeeRequest.class);
    }

    @Override
    public EmployeeRequest findByFirstName(String firstName) {
        EmployeeEntity employeeEntity = employeeRepository.findByFirstName(firstName);
        if (employeeEntity == null) {
            throw new DataNotFoundException("Employee with first name '%s' not found.".formatted(firstName));
        }
        return mapper.map(employeeEntity, EmployeeRequest.class);
    }

    @Override
    public PaginatedResponse<EmployeeResponse> getAllWithPaginated(String searchTerm, Pageable pageable) {
        try {
            List<EmployeeResponse> employeeRequestList = new ArrayList<>();
            Page<EmployeeEntity> response = employeeRepository.findAllWithSearch(searchTerm, pageable);
            response.forEach(employeeEntity ->
                    employeeRequestList.add(mapper.map(employeeEntity, EmployeeResponse.class)));

            return new PaginatedResponse<>(
                    HttpStatus.OK.value(),
                    employeeRequestList.isEmpty() ? "No roles found!" : "Roles retrieved.",
                    employeeRequestList,
                    response.getTotalPages(),
                    response.getTotalElements(),
                    response.getNumber()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UnexpectedException(ERROR_MESSAGE);
        }
    }

    @Override
    public List<EmployeeResponse> getNonManagers() {
        try {
            List<EmployeeResponse> employeeList = new ArrayList<>();
            employeeRepository.findNonManagerEmployees().forEach(employeeEntity ->
                    employeeList.add(mapper.map(employeeEntity, EmployeeResponse.class)));
            return employeeList;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UnexpectedException("An unexpected error occurred while fetching non-managers.");
        }
    }

    @Override
    public List<EmployeeResponse> getEmployeesByDepartmentId(Long id) {
        try {
            if (departmentRepository.existsById(id)) {
                List<EmployeeResponse> employeeRequestList = new ArrayList<>();
                employeeRepository.findByDepartmentId(id).forEach(employeeEntity ->
                        employeeRequestList.add(mapper.map(employeeEntity, EmployeeResponse.class)));
                return employeeRequestList;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UnexpectedException(ERROR_MESSAGE);
        }
        throw new DataNotFoundException("Department with ID %d not found.".formatted(id));
    }

}