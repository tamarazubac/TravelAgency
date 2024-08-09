package com.example.TravelAgency.services.interfaces;

import com.example.TravelAgency.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findByRole(String roleName);
    List<User> findAll();

    Optional<User> create(User user);

    Optional<User> update(User user);

    void delete(Long id);

    void deleteByUsername(String username);

    Optional<User> addRoleToUser(Long userId, String roleName);

    Optional<User> removeRoleFromUser(Long userId, String roleName);

}
