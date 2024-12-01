package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.Address;
import edu.icet.demo.dto.Employee;
import edu.icet.demo.entity.DepartmentEntity;
import edu.icet.demo.entity.EmployeeEntity;
import edu.icet.demo.entity.RoleEntity;
import edu.icet.demo.exception.DataNotFoundException;
import edu.icet.demo.exception.MissingAttributeException;
import edu.icet.demo.repository.DepartmentRepository;
import edu.icet.demo.repository.EmployeeRepository;
import edu.icet.demo.repository.RoleRepository;
import edu.icet.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
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

        validateEmployeeAttributes(employee);
        validateAddressAttributes(employee.getAddress());

        Optional<RoleEntity> roleEntityOptional = roleRepository.findById(employee.getRole().getId());
        if (roleEntityOptional.isEmpty()){
            throw new DataNotFoundException("Role with ID %d not found".formatted(employee.getRole().getId()));
        }

        Optional<DepartmentEntity> departmentEntityOptional =
                departmentRepository.findById(employee.getDepartment().getId());
        if (departmentEntityOptional.isEmpty()){
            throw new DataNotFoundException("Department with ID %d not found"
                    .formatted(employee.getDepartment().getId()));
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

    private void validateEmployeeAttributes(Employee employee) {
        if (employee.getFirstName() == null || employee.getFirstName().isEmpty()) {
            throw new MissingAttributeException("First name is required.");
        }
        if (employee.getLastName() == null || employee.getLastName().isEmpty()) {
            throw new MissingAttributeException("Last name is required.");
        }
        if (employee.getEmail() == null || employee.getEmail().isEmpty()) {
            throw new MissingAttributeException("Email is required.");
        }
        if (employee.getDob() == null) {
            throw new MissingAttributeException("Date of birth is required.");
        }
        if (employee.getPhoneNumber() == null || employee.getPhoneNumber().isEmpty()) {
            throw new MissingAttributeException("Phone number is required.");
        }
        if (employee.getGender() == null || employee.getGender().isEmpty()) {
            throw new MissingAttributeException("Gender is required.");
        }

        if (employee.getRole() == null || employee.getRole().getId() == null) {
            throw new MissingAttributeException("Missing role assignment. Please select a role.");
        }
        if (employee.getDepartment() == null || employee.getDepartment().getId() == null) {
            throw new MissingAttributeException("Missing department assignment. Please select a department.");
        }

    }

    private void validateAddressAttributes(Address address) {
        if (address == null){
            throw new MissingAttributeException("Address details are missing.");
        }
        if (address.getStreet() == null || address.getStreet().isEmpty()) {
            throw new MissingAttributeException("Street is required in Address.");
        }
        if (address.getCity() == null || address.getCity().isEmpty()) {
            throw new MissingAttributeException("City is required in Address.");
        }
        if (address.getState() == null || address.getState().isEmpty()) {
            throw new MissingAttributeException("State is required in Address.");
        }
        if (address.getPostalCode() == null || address.getPostalCode().isEmpty()) {
            throw new MissingAttributeException("Postal code is required in Address.");
        }
        if (address.getCountry() == null || address.getCountry().isEmpty()) {
            throw new MissingAttributeException("Country is required in Address.");
        }
        if (address.getDistrict() == null || address.getDistrict().isEmpty()) {
            throw new MissingAttributeException("District is required in Address.");
        }
    }

}