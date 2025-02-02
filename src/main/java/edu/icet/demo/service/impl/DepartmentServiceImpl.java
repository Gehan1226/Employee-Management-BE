package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.hibernate.exception.ConstraintViolationException;
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
    public Department addDepartment(Department department) {
        if (repository.existsByName(department.getName())) {
            throw new DataDuplicateException(
                    "A department with the name '" + department.getName() + "' already exists.");
        }
        try {
            DepartmentEntity departmentEntity = mapper.convertValue(department, DepartmentEntity.class);
            DepartmentEntity savedEntity = repository.save(departmentEntity);
            return mapper.convertValue(savedEntity, Department.class);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(
                    "A data integrity violation occurred while saving the department");
        } catch (Exception ex) {
            throw new UnexpectedException("An unexpected error occurred while saving the department");
        }
    }
    @Override
    public PaginatedResponse<Department> getAllWithPagination(Pageable pageable, String searchTerm) {
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
    }

    @Override
    public void deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

    @Override
    public List<DepartmentNameAndEmployeeCountDTO> getDepartmentNameWithEmployeeCount() {
        return  repository.findAllDepartmentNamesAndEmployeeCounts();

    }
}