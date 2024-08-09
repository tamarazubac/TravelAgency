package com.example.TravelAgency.services;

import com.example.TravelAgency.models.Role;
import com.example.TravelAgency.models.User;
import com.example.TravelAgency.repositories.IRoleRepository;
import com.example.TravelAgency.repositories.IUserRepository;
import com.example.TravelAgency.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    public IUserRepository userRepository;
    @Autowired
    public IRoleRepository roleRepository;
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findByRole(String roleName) {

            List<User> users = userRepository.findByRolesRoleName(roleName);
            return users.stream().collect(Collectors.toList());

    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> create(User user) {

        Optional<User> exist=userRepository.findByUsername(user.username);
        if(exist.isPresent()){
            return Optional.empty();
        }
        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<User> update(User user) {
        if(userRepository.existsById(user.id)){
            return Optional.of(userRepository.saveAndFlush(user));
        }
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    public Optional<User> addRoleToUser(Long userId, String roleName) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Role> roleOpt = roleRepository.findByRoleName(roleName);

        if (userOpt.isPresent() && roleOpt.isPresent()) {

            User user=userOpt.get();
            user.setRoles(new ArrayList<>());
            userRepository.saveAndFlush(user);  //empty list of roles

            Role role = roleOpt.get();
//            if(!user.getRoles().contains(roleRepository.findByRoleName(role.getRoleName()))) {
            user.getRoles().add(role);
//            }

            System.out.println("Useeeeeeeeeeeeeeeeeeer : "+user.toString());
            userRepository.saveAndFlush(user);
            return Optional.of(user);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> removeRoleFromUser(Long userId, String roleName) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Role> roleOpt = roleRepository.findByRoleName(roleName);

        if (userOpt.isPresent() && roleOpt.isPresent()) {
            User user = userOpt.get();
            Role role = roleOpt.get();

            if (user.getRoles().contains(role)) {
                user.getRoles().remove(role);
                userRepository.saveAndFlush(user);
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }


}
