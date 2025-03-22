package edu.icet.demo.service.impl;

import edu.icet.demo.dto.department.DepartmentCreateRequest;
import edu.icet.demo.dto.department.DepartmentResponse;
import edu.icet.demo.dto.department.DepartmentNameAndEmployeeCount;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.entity.DepartmentEntity;
import edu.icet.demo.entity.EmployeeEntity;
import edu.icet.demo.entity.ManagerEntity;
import edu.icet.demo.exception.DataDuplicateException;
import edu.icet.demo.exception.DataIntegrityException;
import edu.icet.demo.exception.DataNotFoundException;
import edu.icet.demo.exception.UnexpectedException;
import edu.icet.demo.repository.DepartmentRepository;
import edu.icet.demo.repository.EmployeeRepository;
import edu.icet.demo.repository.ManagerRepository;
import edu.icet.demo.repository.RoleRepository;
import edu.icet.demo.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public void addDepartment(DepartmentCreateRequest department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new DataDuplicateException(
                    String.format("A department with the name '%s' already exists.", department.getName())
            );
        }
        ManagerEntity managerEntity = null;

        if (department.getEmployeeId() != null) {
            EmployeeEntity employeeEntity = employeeRepository.findById(department.getEmployeeId())
                    .orElseThrow(() -> new DataNotFoundException(
                            "Employee not found with ID " + department.getEmployeeId()
                    ));
            managerEntity = new ManagerEntity();
            managerEntity.setEmployee(employeeEntity);
            try {
                managerEntity = managerRepository.save(managerEntity);
            } catch (DataIntegrityViolationException ex) {
                throw new DataIntegrityException("Failed to create a manager for the department." +
                        " Please check that all provided values are valid and unique.");
            } catch (Exception ex) {
                throw new UnexpectedException("An unexpected error occurred while creating the manager.");
            }
        }

        try {
            DepartmentEntity departmentEntity = mapper.map(department, DepartmentEntity.class);
            if (managerEntity != null) {
                departmentEntity.setManager(managerEntity);
            }
            departmentRepository.save(departmentEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(
                    "Database constraint violation. Please check that all provided values are valid and unique.");
        } catch (Exception ex) {
            throw new UnexpectedException("An unexpected error occurred while saving the department");
        }
    }

    @Override
    public PaginatedResponse<DepartmentResponse> getAllWithPagination(Pageable pageable, String searchTerm) {
        try {
            List<DepartmentResponse> depList = new ArrayList<>();
            Page<DepartmentEntity> response = departmentRepository.findAllWithSearch(searchTerm, pageable);
            response.forEach(departmentEntity -> {
                DepartmentResponse departmentResponse = mapper.map(departmentEntity, DepartmentResponse.class);
                depList.add(departmentResponse);
            });

            return new PaginatedResponse<>(
                    HttpStatus.OK.value(),
                    depList.isEmpty() ? "No departments found!" : "Departments retrieved.",
                    depList,
                    response.getTotalPages(),
                    response.getTotalElements(),
                    response.getNumber()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UnexpectedException("An unexpected error occurred while retrieving departments");
        }
    }

    @Override
    public void deleteById(Long id) {
        DepartmentEntity department = departmentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        String.format("Department with ID %d does not exist in the system.", id)));
        try {
            department.getRoleList().forEach(roleEntity ->
                    roleEntity.setDepartment(null));
            department.getEmployeeList().forEach(employeeEntity ->
                    employeeEntity.setDepartment(null));
            departmentRepository.delete(department);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UnexpectedException("An unexpected error occurred while deleting the department");
        }
    }

    @Override
    public List<DepartmentNameAndEmployeeCount> getDepartmentNameWithEmployeeCount() {
        try {
            return departmentRepository.findAllDepartmentNamesAndEmployeeCounts();
        } catch (Exception e) {
            throw new UnexpectedException(
                    "An unexpected error occurred while retrieving department names and employee counts"
            );
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateDepartment(Long id, DepartmentCreateRequest departmentCreateRequest) {
        DepartmentEntity departmentEntity = departmentRepository.findById(id)
                .orElseThrow(() -> new DataIntegrityException(
                        String.format("Department with ID %d not found.", id)));

        if (departmentRepository.existsByNameAndIdNot(departmentCreateRequest.getName(), id)) {
            throw new DataIntegrityException("A department with this name already exists.");
        }

        ManagerEntity managerEntity = null;
        if (departmentCreateRequest.getEmployeeId() != null) {
            Optional<ManagerEntity> existingManager =
                    managerRepository.findByEmployeeId(departmentCreateRequest.getEmployeeId());

            if (existingManager.isEmpty()) {
                EmployeeEntity employeeEntity = employeeRepository.findById(departmentCreateRequest.getEmployeeId())
                        .orElseThrow(() -> new DataNotFoundException(
                                "Employee not found with ID " + departmentCreateRequest.getEmployeeId()
                        ));
                managerEntity = new ManagerEntity();
                managerEntity.setEmployee(employeeEntity);
                try {
                    departmentEntity.setManager(managerEntity);
                    managerRepository.save(managerEntity);
                } catch (Exception ex) {
                    throw new UnexpectedException("An unexpected error occurred while creating the manager.");
                }
            } else {
                if (Objects.equals(existingManager.get().getEmployee().getId(),
                        departmentCreateRequest.getEmployeeId()) && !Objects.equals(existingManager.get().getId(), departmentEntity.getManager().getId())) {
                    throw new DataIntegrityException(
                            String.format("The employee with %d is already a manager of another department.",
                                    departmentCreateRequest.getEmployeeId()));
                }
            }
        }

        try {
            mapper.map(departmentCreateRequest, departmentEntity);
            departmentEntity.setId(id);
            departmentRepository.save(departmentEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(
                    "Database constraint violation. Please check that all provided values are valid and unique.");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new UnexpectedException("An unexpected error occurred while updating the department");
        }
    }

    @Override
    public List<DepartmentResponse> getAll() {
        List<DepartmentResponse> departmentList = new ArrayList<>();
        try {
            departmentRepository.findAll().forEach(departmentEntity ->
                    departmentList.add(mapper.map(departmentEntity, DepartmentResponse.class)));
            return departmentList;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UnexpectedException("An unexpected error occurred while retrieving departments");
        }
    }
}