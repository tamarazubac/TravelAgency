package com.example.TravelAgency.controllers;

import com.example.TravelAgency.dtos.UserDTO;
import com.example.TravelAgency.models.Role;
import com.example.TravelAgency.models.User;
import com.example.TravelAgency.services.interfaces.IRoleService;
import com.example.TravelAgency.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    public IUserService userService;
    @Autowired
    public IRoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> findAll(){
        List<UserDTO> result=new ArrayList<>();

        for(User u : userService.findAll()){
            result.add(new UserDTO(u,true));
        }

        return result;
    }

    public Optional<UserDTO> findById(Long id){

        Optional<User> user=userService.findById(id);

        if(user.isPresent()){
            return Optional.of(new UserDTO(user.get(),true));
        }

        return Optional.empty();
    }

    public Optional<UserDTO> findByUsername(String username){

        Optional<User> user=userService.findByUsername(username);

        if(user.isPresent()){
            return Optional.of(new UserDTO(user.get(),true));
        }

        return Optional.empty();
    }

    public Optional<User> create(UserDTO userPostDTO){
        //from postDTO to ordinary

        User newUser=toEntity(userPostDTO,-1L);

        Optional<Role> customer=roleService.findByName("CUSTOMER");

        newUser.roles.add(customer.get());

        //saving encoded password for user
        newUser.password=passwordEncoder.encode(userPostDTO.password);

        return userService.create(newUser);

    }

    public Optional<User> createAdmin(UserDTO userPostDTO){
        //from postDTO to ordinary

        User newUser=toEntity(userPostDTO,-1L);

        Optional<Role> customer=roleService.findByName("ADMIN");

        newUser.roles.add(customer.get());

        //saving encoded password for user
        newUser.password=passwordEncoder.encode(userPostDTO.password);

        return userService.create(newUser);

    }

    public void delete(Long id){



        userService.delete(id);
    }

    public void deleteByUsername(String username){
        userService.deleteByUsername(username);
    }

    public Optional<User> update(UserDTO userDTO, Long id){
        //from putDTO to entity
        return  userService.update(toEntity(userDTO,id));
    }

    public Optional<User> addRoleToUser(Long userId, String roleName) {
        return userService.addRoleToUser(userId, roleName);
    }

    public Optional<User> removeRoleFromUser(Long userId, String roleName) {
        return userService.removeRoleFromUser(userId, roleName);
    }

    public List<UserDTO> findByRole(String roleName) {

        List<UserDTO> result=new ArrayList<>();
        List<User>users=userService.findByRole(roleName);

        for(User u:users){
            result.add(toDTO(u));
        }

        return result;
    }

    public UserDTO toDTO(User u){
        return new UserDTO(u,true);
    }

    public User toEntity(UserDTO userDTO,Long id){

        User result=new User();
        result.roles=new ArrayList<>();
        result.setEmail(userDTO.email);
        result.setPhone(userDTO.phone);
        result.setPassword(userDTO.password);
        result.setUsername(userDTO.username);
        result.setFirstName(userDTO.first_name);
        result.setLastName(userDTO.last_name);

        if(id!=-1L){
            result.id=id;     //set id
        }

        return result;
    }

}
