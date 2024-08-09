package com.example.TravelAgency.rest;
import com.example.TravelAgency.controllers.DestinationController;
import com.example.TravelAgency.dtos.DestinationDTO;
import com.example.TravelAgency.models.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/destinations")
@CrossOrigin(origins = "http://localhost:4200")
public class RestDestinationController {

    @Autowired
    public DestinationController destinationController;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DestinationDTO>> findAll(){
        List<DestinationDTO> result= destinationController.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value="/id/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DestinationDTO> findById(@PathVariable Long id){
        Optional<DestinationDTO> result=destinationController.findById(id);
        if(result.isPresent()){
            return new ResponseEntity<>(result.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Destination>  create(@RequestBody DestinationDTO newDestination){
        Optional<Destination> result = destinationController.create(newDestination);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Destination> update(@RequestBody DestinationDTO arrangement, @PathVariable Long id){
        Optional<Destination> result = destinationController.update(arrangement, id);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Destination> delete(@PathVariable Long id){
        destinationController.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
