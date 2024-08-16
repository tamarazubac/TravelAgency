package com.example.TravelAgency.rest;

import com.example.TravelAgency.controllers.UserController;
import com.example.TravelAgency.dtos.UserDTO;
import com.example.TravelAgency.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class RestUserController {

    @Autowired
    public UserController userController;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDTO>> findAll(){
        List<UserDTO> result= userController.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value="/id/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> findById(@PathVariable Long id){
        Optional<UserDTO> result=userController.findById(id);
        if(result.isPresent()){
            return new ResponseEntity<>(result.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/username/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> findByUsername(@PathVariable String username){
        Optional<UserDTO> result=userController.findByUsername(username);
        if(result.isPresent()){
            return new ResponseEntity<>(result.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<User>  create(@RequestBody UserDTO newUser){
        Optional<User> result = userController.create(newUser);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value="/admin")
    public ResponseEntity<User>  createAdmin(@RequestBody UserDTO newUser){
        Optional<User> result = userController.createAdmin(newUser);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value="/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> delete(@PathVariable Long id){
        userController.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value="username/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> deleteByUsername(@PathVariable String username){
        userController.deleteByUsername(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<User> update(@RequestBody UserDTO user, @PathVariable Long id){
        Optional<User> result = userController.update(user, id);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping(value = "/{userId}/role/{roleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> addRoleToUser(@PathVariable Long userId, @PathVariable String roleName) {
        Optional<User> result = userController.addRoleToUser(userId, roleName);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value = "/remove/{userId}/role/{roleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> removeRoleFromUser(@PathVariable Long userId, @PathVariable String roleName) {
        Optional<User> result = userController.removeRoleFromUser(userId, roleName);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/role/{roleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SALESMAN','ADMIN')")
    public ResponseEntity<List<UserDTO>> findByRole(@PathVariable String roleName) {
        List<UserDTO> result = userController.findByRole(roleName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
