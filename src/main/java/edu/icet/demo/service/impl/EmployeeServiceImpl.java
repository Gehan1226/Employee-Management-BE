package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.Employee;
import edu.icet.demo.entity.DepartmentEntity;
import edu.icet.demo.entity.EmployeeEntity;
import edu.icet.demo.entity.RoleEntity;
import edu.icet.demo.repository.DepartmentRepository;
import edu.icet.demo.repository.EmployeeRepository;
import edu.icet.demo.repository.RoleRepository;
import edu.icet.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    final EmployeeRepository employeeRepository;
    final RoleRepository roleRepository;
    final DepartmentRepository departmentRepository;
    private final ObjectMapper mapper;

    @Override
    public Employee addEmployee(Employee employee, Long roleId,  Long departmentId) {
        Optional<RoleEntity> roleEntityOptional = roleRepository.findById(roleId);
        Optional<DepartmentEntity> departmentEntityOptional = departmentRepository.findById(departmentId);
        EmployeeEntity employeeEntity = mapper.convertValue(employee, EmployeeEntity.class);

        if (roleEntityOptional.isPresent()) {
            RoleEntity roleEntity = roleEntityOptional.get();
            employeeEntity.setRole(roleEntity);
        }
        if (departmentEntityOptional.isPresent()){
            DepartmentEntity departmentEntity = departmentEntityOptional.get();
            employeeEntity.setDepartment(departmentEntity);
        }
        return mapper.convertValue(employeeRepository.save(employeeEntity), Employee.class);
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employeeList = new ArrayList<>();

        employeeRepository.findAll().forEach(employeeEntity ->
                employeeList.add(new ObjectMapper().convertValue(employeeEntity, Employee.class)
                ));
        return employeeList;
    }

    @Override
    public void deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)){
            employeeRepository.deleteById(id);
        }
    }

    @Override
    public void updateEmployee(Employee employee) {
        if (employeeRepository.existsById(employee.getId())){
            employeeRepository.save(new ObjectMapper().convertValue(employee, EmployeeEntity.class));
        }
    }

    @Override
    public Employee findById(Long id) {
        if (employeeRepository.findById(id).isPresent()){
            return new ObjectMapper().convertValue(
                    employeeRepository.findById(id).get(), Employee.class
            );
        }
        return new Employee();
    }

    @Override
    public Employee findByFirstName(String firstName) {
        return new ObjectMapper().convertValue(employeeRepository.findByFirstName(firstName), Employee.class);
    }
}