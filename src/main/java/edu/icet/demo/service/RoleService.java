package edu.icet.demo.service;

import edu.icet.demo.dto.role.RoleResponse;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.dto.role.RoleRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    void addRole(RoleRequest role);

    List<RoleResponse> getAll();

    void deleteRoleById(Long id);

    List<RoleResponse> getRolesByDepartmentId(Long id);

    PaginatedResponse<RoleResponse> getAllWithPagination(String searchTerm, Pageable pageable);
}