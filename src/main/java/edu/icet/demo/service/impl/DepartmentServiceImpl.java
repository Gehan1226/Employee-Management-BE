package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.operationDTOS.DepartmentOperationDTO;
import edu.icet.demo.dto.Department;
import edu.icet.demo.dto.DepartmentNameAndEmployeeCountDTO;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.entity.DepartmentEntity;
import edu.icet.demo.entity.EmployeeEntity;
import edu.icet.demo.exception.DataDuplicateException;
import edu.icet.demo.exception.DataIntegrityException;
import edu.icet.demo.exception.UnexpectedException;
import edu.icet.demo.repository.DepartmentRepository;
import edu.icet.demo.repository.EmployeeRepository;
import edu.icet.demo.service.DepartmentService;
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
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ObjectMapper mapper;

    @Override
    public DepartmentOperationDTO addDepartment(DepartmentOperationDTO department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new DataDuplicateException(
                    String.format("A department with the name '%s' already exists.", department.getName())
            );
        }
        try {
            DepartmentEntity departmentEntity = mapper.convertValue(department, DepartmentEntity.class);
            departmentRepository.save(departmentEntity);
            return department;
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(
                    "Database constraint violation. Please check that all provided values are valid and unique.");
        } catch (Exception ex) {
            throw new UnexpectedException("An unexpected error occurred while saving the department");
        }
    }

    @Override
    public PaginatedResponse<Department> getAllWithPagination(Pageable pageable, String searchTerm) {
        try {
            List<Department> depList = new ArrayList<>();
            Page<DepartmentEntity> response = departmentRepository.findAllWithSearch(searchTerm, pageable);
            response.forEach(departmentEntity ->
                    depList.add(mapper.convertValue(departmentEntity, Department.class)));

            return new PaginatedResponse<>(
                    HttpStatus.OK.value(),
                    depList.isEmpty() ? "No departments found!" : "Departments retrieved.",
                    depList,
                    response.getTotalPages(),
                    response.getTotalElements(),
                    response.getNumber()
            );
        } catch (Exception e) {
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
    public DepartmentOperationDTO updateDepartment(Long id, DepartmentOperationDTO department) {
        DepartmentEntity departmentEntity = departmentRepository.findById(id)
                .orElseThrow(() -> new DataIntegrityException(
                        String.format("Department with ID %d does not exist in the system.", id)));
        EmployeeEntity employeeEntity = employeeRepository.findById(department.getManager().id())
                .orElseThrow(() -> new DataIntegrityException(
                        String.format("Manager with ID %d does not exist in the system.", department.getManager().id())
                ));
        try {
            departmentEntity.setName(department.getName());
            departmentEntity.setResponsibility(department.getResponsibility());
            departmentEntity.setManager(employeeEntity);
            departmentRepository.save(departmentEntity);
            return department;
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(
                    "Database constraint violation. Please check that all provided values are valid and unique.");
        } catch (Exception ex) {
            throw new UnexpectedException("An unexpected error occurred while updating the department");
        }
    }
}