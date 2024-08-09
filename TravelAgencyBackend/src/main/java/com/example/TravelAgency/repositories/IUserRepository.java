package com.example.TravelAgency.repositories;

import com.example.TravelAgency.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    void deleteByUsername(String username);

    List<User> findByRolesRoleName(String roleName);

}
