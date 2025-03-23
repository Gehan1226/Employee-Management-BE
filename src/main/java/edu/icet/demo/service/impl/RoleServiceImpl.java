package edu.icet.demo.service.impl;

import edu.icet.demo.dto.role.RoleResponse;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.dto.role.RoleRequest;
import edu.icet.demo.entity.DepartmentEntity;
import edu.icet.demo.entity.RoleEntity;
import edu.icet.demo.exception.DataDuplicateException;
import edu.icet.demo.exception.DataIntegrityException;
import edu.icet.demo.exception.UnexpectedException;
import edu.icet.demo.repository.DepartmentRepository;
import edu.icet.demo.repository.RoleRepository;
import edu.icet.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper mapper;

    @Override
    public void addRole(RoleRequest role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new DataDuplicateException(
                    String.format("A role with the name '%s' already exists.", role.getName())
            );
        }
        if (!departmentRepository.existsById(role.getDepartmentId())) {
            throw new DataIntegrityException("Department with id " + role.getDepartmentId() + " does not exist.");
        }
        try {
            RoleEntity roleEntity = mapper.map(role, RoleEntity.class);
            roleEntity.setDepartment(DepartmentEntity.builder().id(role.getDepartmentId()).build());
            roleRepository.save(roleEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(
                    "Database constraint violation. Please check that all provided values are valid and unique.");
        } catch (Exception ex) {
            throw new UnexpectedException("An unexpected error occurred while saving the role");
        }
    }

    @Override
    public List<RoleResponse> getAll() {
        try {
            List<RoleResponse> roleResponseList = new ArrayList<>();
            roleRepository.findAll().forEach(obj -> roleResponseList.add(mapper.map(obj, RoleResponse.class)));
            return roleResponseList;
        } catch (Exception ex) {
            throw new UnexpectedException("An unexpected error occurred while fetching roles");
        }
    }

    @Override
    public void deleteRoleById(Long id) {
        RoleEntity roleEntity = roleRepository.findById(id)
                .orElseThrow(() -> new DataIntegrityException("Role not found with ID " + id));
        try {
            roleEntity.getEmployeeList().forEach(employeeEntity -> employeeEntity.setRole(null));
            roleRepository.deleteById(id);
        } catch (Exception ex) {
            throw new UnexpectedException("An unexpected error occurred while deleting the role");
        }
    }

    @Override
    public List<RoleResponse> getRolesByDepartmentId(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new DataIntegrityException("Department with id " + id + " does not exist.");
        }
        try{
            List<RoleResponse> roleResponseList = new ArrayList<>();
            roleRepository.findByDepartmentId(id).forEach(roleEntity ->
                    roleResponseList.add(mapper.map(roleEntity, RoleResponse.class)
                    ));
            return roleResponseList;
        } catch (Exception ex) {
            throw new UnexpectedException("An unexpected error occurred while fetching roles");
        }
    }

    @Override
    public PaginatedResponse<RoleResponse> getAllWithPagination(String searchTerm, Pageable pageable) {
        List<RoleResponse> roleResponseList = new ArrayList<>();
        Page<RoleEntity> response = roleRepository.findAllWithSearch(searchTerm, pageable);
        response.forEach(roleEntity ->
                roleResponseList.add(mapper.map(roleEntity, RoleResponse.class)));

        return new PaginatedResponse<>(
                HttpStatus.OK.value(),
                roleResponseList.isEmpty() ? "No roles found!" : "Roles retrieved.",
                roleResponseList,
                response.getTotalPages(),
                response.getTotalElements(),
                response.getNumber()
        );
    }
}