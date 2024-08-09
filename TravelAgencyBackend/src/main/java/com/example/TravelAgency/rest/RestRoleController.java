package com.example.TravelAgency.rest;

import com.example.TravelAgency.controllers.RoleController;
import com.example.TravelAgency.dtos.RoleDTO;
import com.example.TravelAgency.models.Role;
import com.example.TravelAgency.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/roles")
@CrossOrigin(origins = "http://localhost:4200")
public class RestRoleController {

    @Autowired
    public RoleController roleController;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Role>> findAll(){
        List<Role> result= roleController.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value="/id/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Role> findById(@PathVariable Long id){
        Optional<Role> result=roleController.findById(id);
        if(result.isPresent()){
            return new ResponseEntity<>(result.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Role>  create(@RequestBody RoleDTO newRole){
        Optional<Role> result = roleController.create(newRole);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/name/{name}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Role> findByName(@PathVariable String name){
        Optional<Role> result=roleController.findByName(name);
        if(result.isPresent()){
            return new ResponseEntity<>(result.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



}
