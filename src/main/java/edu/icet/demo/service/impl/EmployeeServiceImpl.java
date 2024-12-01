package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.Employee;
import edu.icet.demo.entity.DepartmentEntity;
import edu.icet.demo.entity.EmployeeEntity;
import edu.icet.demo.entity.RoleEntity;
import edu.icet.demo.exception.MissingAttributeException;
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

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final ObjectMapper mapper;

    @Override
    public Employee addEmployee(Employee employee) {

        if (employee.getRole() == null || employee.getRole().getId() == null){
            throw new MissingAttributeException("Missing role assignment. Please select a role.");
        }

        Optional<RoleEntity> roleEntityOptional = roleRepository.findById(employee.getRole().getId());
        if (roleEntityOptional.isEmpty()){
        }

        Optional<DepartmentEntity> departmentEntityOptional =
                departmentRepository.findById(employee.getDepartment().getId());
        if (departmentEntityOptional.isEmpty()){
            throw new MissingAttributeException("Missing department assignment. Please select a department.");
        }

        EmployeeEntity employeeEntity = mapper.convertValue(employee, EmployeeEntity.class);
        employeeEntity.setRole(roleEntityOptional.get());
        employeeEntity.setDepartment(departmentEntityOptional.get());
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