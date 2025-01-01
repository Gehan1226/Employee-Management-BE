package edu.icet.demo.service;

import edu.icet.demo.dto.Role;

import java.util.List;

public interface RoleService {
    void addRole(Role role);

    List<Role> getAll();

    boolean deleteRoleById(Long id);

    List<Role> getRolesByDepartmentId(Long id);
}