package com.example.TravelAgency.services;

import com.example.TravelAgency.models.Role;
import com.example.TravelAgency.repositories.IRoleRepository;
import com.example.TravelAgency.services.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService implements IRoleService {

    @Autowired
    public IRoleRepository roleRepository;

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> create(Role newRole) {
        return Optional.of(roleRepository.save(newRole));
    }

    @Override
    public Optional<Role> findByName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
