package edu.icet.demo.controller;

import edu.icet.demo.dto.Role;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.dto.response.SuccessResponseWithData;
import edu.icet.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
@CrossOrigin
public class RoleController {

    private final RoleService service;

    @PostMapping("/add-role")
    public void addRole(@RequestBody Role role){
        service.addRole(role);
    }

    @GetMapping("/get-all")
    public List<Role> getRoles(){
        return service.getAll();
    }

    @GetMapping("/get-all-paginated")
    public PaginatedResponse<Role> getRolesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm) {

        Pageable pageable = PageRequest.of(page, size);
        return service.getAllWithPagination(searchTerm, pageable);
    }

    @GetMapping("/get-by-department/{id}")
    public SuccessResponseWithData<List<Role>> getRolesByDepartmentId(@PathVariable Long id){
        List<Role> rolesByDepartmentId = service.getRolesByDepartmentId(id);
        String message = rolesByDepartmentId.isEmpty() ? "Roles not found for this department!" : "Roles retrieved.";
        return SuccessResponseWithData.<List<Role>>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(rolesByDepartmentId)
                .build();
    }

    @DeleteMapping("/delete-by-id/{id}")
    public boolean deleteRoleById(@PathVariable Long id){
        return service.deleteRoleById(id);
    }
}