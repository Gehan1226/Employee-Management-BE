package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.department.DepartmentRequest;
import edu.icet.demo.dto.department.DepartmentResponse;
import edu.icet.demo.dto.DepartmentNameAndEmployeeCountDTO;
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
import edu.icet.demo.service.DepartmentService;
import jakarta.transaction.Transactional;
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
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;
    private final ObjectMapper mapper;

    @Override
    @Transactional
    public void addDepartment(DepartmentRequest department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new DataDuplicateException(
                    String.format("A department with the name '%s' already exists.", department.getName())
            );
        }
        EmployeeEntity employeeEntity = employeeRepository.findById(department.getEmployeeId())
                .orElseThrow(() ->
                        new DataNotFoundException("Employee not found with ID " + department.getEmployeeId()));

        ManagerEntity managerEntity = new ManagerEntity();
        managerEntity.setEmployee(employeeEntity);
        try {
            managerEntity = managerRepository.save(managerEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException("Failed to create a manager for the department." +
                    "Please check that all provided values are valid and unique.");
        } catch (Exception ex) {
            throw new UnexpectedException("An unexpected error occurred while creating the manager.");
        }

        try {
            DepartmentEntity departmentEntity = mapper.convertValue(department, DepartmentEntity.class);
            departmentEntity.setManager(managerEntity);
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
        try {
            if (departmentRepository.existsById(id)) {
                departmentRepository.deleteById(id);
                return;
            }
        } catch (Exception e) {
            throw new UnexpectedException("An unexpected error occurred while deleting the department");
        }
        throw new DataIntegrityException(String.format("Department with ID %d does not exist in the system.", id));
    }

    @Override
    public List<DepartmentNameAndEmployeeCountDTO> getDepartmentNameWithEmployeeCount() {
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