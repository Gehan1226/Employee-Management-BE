package edu.icet.demo.service;

import edu.icet.demo.dto.Role;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.dto.role.RoleRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    void addRole(RoleRequest role);

    List<Role> getAll();

    boolean deleteRoleById(Long id);

    List<Role> getRolesByDepartmentId(Long id);

    PaginatedResponse<Role> getAllWithPagination(String searchTerm, Pageable pageable);
}