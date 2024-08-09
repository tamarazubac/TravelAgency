package com.example.TravelAgency.rest;

import com.example.TravelAgency.controllers.ArrangementController;
import com.example.TravelAgency.dtos.ArrangementDTO;
import com.example.TravelAgency.models.Arrangement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/arrangements")
@CrossOrigin(origins = "http://localhost:4200")
public class RestArrangementController {

    @Autowired
    public ArrangementController arrangementController;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArrangementDTO>> findAll(){
        List<ArrangementDTO> result= arrangementController.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value="/id/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrangementDTO> findById(@PathVariable Long id){
        Optional<ArrangementDTO> result=arrangementController.findById(id);
        if(result.isPresent()){
            return new ResponseEntity<>(result.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SALESMAN')")
    public ResponseEntity<Arrangement>  create(@RequestBody ArrangementDTO newArrangement){
        Optional<Arrangement> result = arrangementController.create(newArrangement);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value="/{id}")
    @PreAuthorize("hasAuthority('SALESMAN')")
    public ResponseEntity<Arrangement> delete(@PathVariable Long id){
        arrangementController.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping(value="/{id}")
    @PreAuthorize("hasAuthority('SALESMAN')")
    public ResponseEntity<Arrangement> update(@RequestBody ArrangementDTO arrangement, @PathVariable Long id){
        Optional<Arrangement> result = arrangementController.update(arrangement, id);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value="/dates/destination/{dateFrom}/{dateTo}/{destinationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArrangementDTO>> findByDestinationAndDates(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFrom,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateTo,
            @PathVariable Long destinationId) {
        List<ArrangementDTO> result = arrangementController.findByDestinationAndDates(dateFrom, dateTo, destinationId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value="/dates/{dateFrom}/{dateTo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArrangementDTO>> findByDates(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFrom,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateTo
            ) {
        List<ArrangementDTO> result = arrangementController.findByDates(dateFrom, dateTo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value="/destination/{destinationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArrangementDTO>> findByDestination( @PathVariable Long destinationId) {
        List<ArrangementDTO> result = arrangementController.findByDestination(destinationId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
