package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.Employee;
import edu.icet.demo.entity.DepartmentEntity;
import edu.icet.demo.entity.EmployeeEntity;
import edu.icet.demo.entity.RoleEntity;
import edu.icet.demo.exception.DataNotFoundException;
import edu.icet.demo.repository.AddressRepository;
import edu.icet.demo.repository.DepartmentRepository;
import edu.icet.demo.repository.EmployeeRepository;
import edu.icet.demo.repository.RoleRepository;
import edu.icet.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final AddressRepository addressRepository;
    private final ObjectMapper mapper;

    @Override
    public Employee addEmployee(Employee employee) {

        RoleEntity roleEntity = roleRepository.findById(employee.getRoleId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Role with ID %d not found.".formatted(employee.getRoleId())));

        DepartmentEntity departmentEntity = departmentRepository.findById(employee.getDepartmentId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Department with ID %d not found.".formatted(employee.getDepartmentId())));

        EmployeeEntity employeeEntity = mapper.convertValue(employee, EmployeeEntity.class);
        employeeEntity.setRole(roleEntity);
        employeeEntity.setDepartment(departmentEntity);
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
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
        }
    }

    @Override
    public Employee updateEmployee(Employee employee) {

//        validateEmployeeAttributes(employee);
//        validateAddressAttributes(employee.getAddress());
//
//        Long addressId = employee.getAddress().getId();
//        if (addressId == null) {
//            throw new MissingAttributeException("Address ID is missing.");
//        }
//        if (!addressRepository.existsById(addressId)) {
//            throw new DataNotFoundException("Address with ID %d not found".formatted(employee.getAddress().getId()));
//        }
//
//        RoleEntity roleEntity = roleRepository.findById(employee.getRoleId())
//                .orElseThrow(() -> new DataNotFoundException(
//                        "Role with ID %d not found.".formatted(employee.getRoleId())));
//
//        DepartmentEntity departmentEntity = departmentRepository.findById(employee.getDepartmentId())
//                .orElseThrow(() -> new DataNotFoundException(
//                        "Department with ID %d not found.".formatted(employee.getDepartmentId())));
//
//        EmployeeEntity employeeEntity = mapper.convertValue(employee, EmployeeEntity.class);
//        employeeEntity.setRole(roleEntity);
//        employeeEntity.setDepartment(departmentEntity);
//
//        if (employeeRepository.existsById(employee.getId())) {
//            return mapper.convertValue(employeeRepository.save(employeeEntity), Employee.class);
//        }
//        throw new DataNotFoundException("Employee with ID %d not found".formatted(employee.getId()));
        return null;
    }

    @Override
    public Employee findById(Long id) {
        if (employeeRepository.findById(id).isPresent()) {
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