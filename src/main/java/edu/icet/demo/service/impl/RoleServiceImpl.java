package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.Role;
import edu.icet.demo.entity.RoleEntity;
import edu.icet.demo.repository.RoleRepository;
import edu.icet.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    final RoleRepository repository;
    private final ObjectMapper mapper;

    @Override
    public void addRole(Role role) {
        repository.save(mapper.convertValue(role, RoleEntity.class));
    }

    @Override
    public List<Role> getAll() {
        List<Role> roleList= new ArrayList<>();
        repository.findAll().forEach(obj-> roleList.add(mapper.convertValue(obj, Role.class)));
        return roleList;
    }

    @Override
    public boolean deleteRoleById(Long id) {
        repository.deleteById(id);
        return true;
    }
}