package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.Department;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.entity.DepartmentEntity;
import edu.icet.demo.repository.DepartmentRepository;
import edu.icet.demo.service.DepartmentService;
import lombok.RequiredArgsConstructor;
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
        DepartmentEntity saved = repository.save(mapper.convertValue(department, DepartmentEntity.class));
        return mapper.convertValue(saved, Department.class);
    }

    @Override
    public PaginatedResponse<Department> getAllWithPagination(Pageable pageable) {
        List<Department> depList = new ArrayList<>();
        Page<DepartmentEntity> response = repository.findAll(pageable);
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
        if(repository.existsById(id)){
            repository.deleteById(id);
        }
    }
}