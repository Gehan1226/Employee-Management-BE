package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.role.RoleResponse;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.dto.role.RoleRequest;
import edu.icet.demo.entity.DepartmentEntity;
import edu.icet.demo.entity.RoleEntity;
import edu.icet.demo.exception.DataDuplicateException;
import edu.icet.demo.exception.DataIntegrityException;
import edu.icet.demo.exception.UnexpectedException;
import edu.icet.demo.repository.RoleRepository;
import edu.icet.demo.service.RoleService;
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
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final ObjectMapper mapper;

    @Override
    public void addRole(RoleRequest role) {
        if (repository.existsByName(role.getName())) {
            throw new DataDuplicateException(
                    String.format("A role with the name '%s' already exists.", role.getName())
            );
        }
        try {
            RoleEntity roleEntity = mapper.convertValue(role, RoleEntity.class);
            roleEntity.setDepartment(new DepartmentEntity(role.getDepartment().id()));
            repository.save(roleEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(
                    "Database constraint violation. Please check that all provided values are valid and unique.");
        } catch (Exception ex) {
            throw new UnexpectedException("An unexpected error occurred while saving the role");
        }
    }

    @Override
    public List<RoleResponse> getAll() {
        List<RoleResponse> roleResponseList = new ArrayList<>();
        repository.findAll().forEach(obj -> roleResponseList.add(mapper.convertValue(obj, RoleResponse.class)));
        return roleResponseList;
    }

    @Override
    public boolean deleteRoleById(Long id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public List<RoleResponse> getRolesByDepartmentId(Long id) {
        List<RoleResponse> roleResponseList = new ArrayList<>();
        repository.findByDepartmentId(id).forEach(roleEntity ->
                roleResponseList.add(new ObjectMapper().convertValue(roleEntity, RoleResponse.class)
                ));
        return roleResponseList;
    }

    @Override
    public PaginatedResponse<RoleResponse> getAllWithPagination(String searchTerm, Pageable pageable) {
        List<RoleResponse> roleResponseList = new ArrayList<>();
        Page<RoleEntity> response = repository.findAllWithSearch(searchTerm, pageable);
        response.forEach(roleEntity ->
                roleResponseList.add(mapper.convertValue(roleEntity, RoleResponse.class)));

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