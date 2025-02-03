package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.operationDTOS.DepartmentOperationDTO;
import edu.icet.demo.dto.Department;
import edu.icet.demo.dto.DepartmentNameAndEmployeeCountDTO;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.entity.DepartmentEntity;
import edu.icet.demo.exception.DataDuplicateException;
import edu.icet.demo.exception.DataIntegrityException;
import edu.icet.demo.exception.UnexpectedException;
import edu.icet.demo.repository.DepartmentRepository;
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

    private final DepartmentRepository repository;
    private final ObjectMapper mapper;

    @Override
    public DepartmentOperationDTO addDepartment(DepartmentOperationDTO department) {
        if (repository.existsByName(department.getName())) {
            throw new DataDuplicateException(
                    String.format("A department with the name '%s' already exists.", department.getName())
            );
        }
        try {
            DepartmentEntity departmentEntity = mapper.convertValue(department, DepartmentEntity.class);
            repository.save(departmentEntity);
            return department;
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(
                    "Manager ID does not exist in the system. Please provide a valid manager ID.");
        } catch (Exception ex) {
            throw new UnexpectedException("An unexpected error occurred while saving the department");
        }
    }

    @Override
    public PaginatedResponse<Department> getAllWithPagination(Pageable pageable, String searchTerm) {
        try {
            List<Department> depList = new ArrayList<>();
            Page<DepartmentEntity> response = repository.findAllWithSearch(searchTerm, pageable);
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
            if (repository.existsById(id)) {
                repository.deleteById(id);
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
            return repository.findAllDepartmentNamesAndEmployeeCounts();
        } catch (Exception e) {
            throw new UnexpectedException(
                    "An unexpected error occurred while retrieving department names and employee counts"
            );
        }
    }
}