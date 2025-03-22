package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.department.DepartmentRequest;
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
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;
    private final RoleRepository roleRepository;
    private final ObjectMapper mapper;

    @Override
    @Transactional
    public void addDepartment(DepartmentRequest department) {
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
            DepartmentEntity departmentEntity = mapper.convertValue(department, DepartmentEntity.class);
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
                DepartmentResponse departmentResponse = mapper.convertValue(departmentEntity, DepartmentResponse.class);
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
    public DepartmentRequest updateDepartment(Long id, DepartmentRequest department) {
//        DepartmentEntity departmentEntity = departmentRepository.findById(id)
//                .orElseThrow(() -> new DataIntegrityException(
//                        String.format("Department with ID %d does not exist in the system.", id)));
//        EmployeeEntity employeeEntity = employeeRepository.findById(department.getManager().id())
//                .orElseThrow(() -> new DataIntegrityException(
//                        String.format("Manager with ID %d does not exist in the system.", department.getManager().id())
//                ));
//        try {
//            departmentEntity.setName(department.getName());
//            departmentEntity.setResponsibility(department.getResponsibility());
////            departmentEntity.setManager(employeeEntity);
//            departmentRepository.save(departmentEntity);
//            return department;
//        } catch (DataIntegrityViolationException ex) {
//            throw new DataIntegrityException(
//                    "Database constraint violation. Please check that all provided values are valid and unique.");
//        } catch (Exception ex) {
//            throw new UnexpectedException("An unexpected error occurred while updating the department");
//        }
        return null;
    }

    @Override
    public List<DepartmentResponse> getAll() {
        List<DepartmentResponse> departmentList = new ArrayList<>();
        try {
            departmentRepository.findAll().forEach(departmentEntity ->
                    departmentList.add(mapper.convertValue(departmentEntity, DepartmentResponse.class)));
            return departmentList;
        } catch (Exception e) {
            throw new UnexpectedException("An unexpected error occurred while retrieving departments");
        }
    }
}