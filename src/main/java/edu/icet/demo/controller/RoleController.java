package edu.icet.demo.controller;

import edu.icet.demo.dto.Role;
import edu.icet.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@CrossOrigin
public class RoleController {

    final RoleService service;
    @PostMapping("/add-role")
    public void addRole(@RequestBody Role role){
        service.addRole(role);
    }
    @GetMapping("/get-all")
    public List<Role> getRoles(){
        return service.getAll();
    }

    @DeleteMapping("/delete-by-id/{id}")
    public boolean deleteRoleById(@PathVariable Long id){
        return service.deleteRoleById(id);
    }
}