package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.Role;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.entity.RoleEntity;
import edu.icet.demo.repository.RoleRepository;
import edu.icet.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
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
    public void addRole(Role role) {
        repository.save(mapper.convertValue(role, RoleEntity.class));
    }

    @Override
    public List<Role> getAll() {
        List<Role> roleList = new ArrayList<>();
        repository.findAll().forEach(obj -> roleList.add(mapper.convertValue(obj, Role.class)));
        return roleList;
    }

    @Override
    public boolean deleteRoleById(Long id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public List<Role> getRolesByDepartmentId(Long id) {
        List<Role> roleList = new ArrayList<>();
        repository.findByDepartmentId(id).forEach(roleEntity ->
                roleList.add(new ObjectMapper().convertValue(roleEntity, Role.class)
                ));
        return roleList;
    }

    @Override
    public PaginatedResponse<Role> getAllWithPagination(String searchTerm, Pageable pageable) {
        List<Role> roleList = new ArrayList<>();
        Page<RoleEntity> response = repository.findAllWithSearch(searchTerm, pageable);
        response.forEach(roleEntity ->
                roleList.add(mapper.convertValue(roleEntity, Role.class)));

        return new PaginatedResponse<>(
                HttpStatus.OK.value(),
                roleList.isEmpty() ? "No roles found!" : "Roles retrieved.",
                roleList,
                response.getTotalPages(),
                response.getTotalElements(),
                response.getNumber()
        );
    }
}