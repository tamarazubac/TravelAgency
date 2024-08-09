package com.example.TravelAgency.repositories;

import com.example.TravelAgency.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(String roleName);
}
