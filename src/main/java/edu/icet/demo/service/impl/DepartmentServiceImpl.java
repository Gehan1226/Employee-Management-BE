package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.Department;
import edu.icet.demo.entity.DepartmentEntity;
import edu.icet.demo.repository.DepartmentRepository;
import edu.icet.demo.service.DepartmentService;
import lombok.RequiredArgsConstructor;
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
    public List<Department> getAll() {
        Iterable<DepartmentEntity> all = repository.findAll();
        List<Department> depList = new ArrayList<>();
        all.forEach(departmentEntity -> depList.add(mapper.convertValue(departmentEntity, Department.class)));
        return depList;
    }

    @Override
    public void deleteById(Long id) {
        if(repository.existsById(id)){
            repository.deleteById(id);
        }
    }
}