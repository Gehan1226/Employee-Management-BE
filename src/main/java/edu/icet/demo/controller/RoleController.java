package edu.icet.demo.controller;

import edu.icet.demo.dto.role.RoleResponse;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.dto.response.SuccessResponse;
import edu.icet.demo.dto.response.SuccessResponseWithData;
import edu.icet.demo.dto.role.RoleRequest;
import edu.icet.demo.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
@Validated
public class RoleController {

    private final RoleService service;

    @PostMapping()
    public SuccessResponse addRole(@Valid @RequestBody RoleRequest role, BindingResult result) {
        service.addRole(role);
        return SuccessResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Role added successfully.")
                .build();
    }

    @GetMapping()
    public SuccessResponseWithData<List<RoleResponse>> getRoles() {
        List<RoleResponse> roles = service.getAll();
        return SuccessResponseWithData.<List<RoleResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Roles retrieved.")
                .data(roles)
                .build();
    }

    @GetMapping("/get-all-paginated")
    public PaginatedResponse<RoleResponse> getRolesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm) {

        Pageable pageable = PageRequest.of(page, size);
        return service.getAllWithPagination(searchTerm, pageable);
    }

    @GetMapping("/by-department/{id}")
    public SuccessResponseWithData<List<RoleResponse>> getRolesByDepartmentId(@PathVariable Long id) {
        List<RoleResponse> rolesByDepartmentId = service.getRolesByDepartmentId(id);
        String message = rolesByDepartmentId.isEmpty() ? "Roles not found for this department!" : "Roles retrieved.";
        return SuccessResponseWithData.<List<RoleResponse>>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(rolesByDepartmentId)
                .build();
    }

    @DeleteMapping("/{id}")
    public SuccessResponse deleteRoleById(@PathVariable Long id) {
        service.deleteRoleById(id);
        return SuccessResponse.builder().status(HttpStatus.OK.value()).message("Role deleted successfully!").build();
    }

    @PatchMapping("/{id}")
    public SuccessResponse updateRole(@PathVariable Long id, @RequestBody RoleRequest roleRequest) {
        service.updateRole(id, roleRequest);
        return SuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Role updated successfully!")
                .build();
    }
}