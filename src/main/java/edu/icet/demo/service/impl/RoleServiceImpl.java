package edu.icet.demo.service.impl;

import edu.icet.demo.dto.role.RoleResponse;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.dto.role.RoleRequest;
import edu.icet.demo.entity.DepartmentEntity;
import edu.icet.demo.entity.RoleEntity;
import edu.icet.demo.exception.DataDuplicateException;
import edu.icet.demo.exception.DataIntegrityException;
import edu.icet.demo.exception.DataNotFoundException;
import edu.icet.demo.exception.UnexpectedException;
import edu.icet.demo.repository.DepartmentRepository;
import edu.icet.demo.repository.RoleRepository;
import edu.icet.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
            throw new DataNotFoundException(
                    String.format("Department with id %d does not exist.", role.getDepartmentId()));
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
                .orElseThrow(() -> new DataNotFoundException("Role not found with ID " + id));
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
            throw new DataNotFoundException("Department with id " + id + " does not exist.");
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

    @Override
    public void updateRole(Long id, RoleRequest roleRequest) {
        RoleEntity roleEntity = roleRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Role not found with Id " + id));
        if (roleRepository.existsByNameAndIdNot(roleRequest.getName(), id)) {
            throw new DataDuplicateException(
                    String.format("A role with the name '%s' already exists.", roleRequest.getName())
            );
        }
        DepartmentEntity departmentEntity = null;
        if (roleRequest.getDepartmentId() != null) {
            departmentEntity = departmentRepository.findById(roleRequest.getDepartmentId())
                    .orElseThrow(() -> new DataNotFoundException(
                            String.format("Department with id %d does not exist.", roleRequest.getDepartmentId())
                    ));
            roleEntity.setDepartment(departmentEntity);
        }
        try {
            mapper.map(roleRequest, roleEntity);
            roleEntity.setId(id);
            roleRepository.save(roleEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(
                    "Database constraint violation. Please check that all provided values are valid and unique.");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new UnexpectedException("An unexpected error occurred while updating the role");
        }
    }
}