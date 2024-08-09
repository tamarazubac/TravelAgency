package com.example.TravelAgency.controllers;


import com.example.TravelAgency.dtos.RoleDTO;
import com.example.TravelAgency.models.Role;
import com.example.TravelAgency.models.User;
import com.example.TravelAgency.services.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class RoleController {

    @Autowired
    public IRoleService roleService;

    public List<Role> findAll(){
        return roleService.findAll();
    }

    public Optional<Role> findById(Long id){
        return roleService.findById(id);
    }

    public Optional<Role> create(RoleDTO rolePostDTO){
        //from postDTO to ordinary

        return roleService.create(toEntity(rolePostDTO));

    }

    public Optional<Role> findByName(String roleName){
        return roleService.findByName(roleName);
    }

    public Role toEntity(RoleDTO rolePostDTO) {
        Role role = new Role();
        role.setRoleName(rolePostDTO.roleName);
        return role;
    }




}
