package com.example.TravelAgency.services.interfaces;

import com.example.TravelAgency.models.Destination;
import com.example.TravelAgency.models.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {

    Optional<Role> findById(Long id);

    List<Role> findAll();

    Optional<Role> create(Role newRole);

    Optional<Role> findByName(String roleName);

}
