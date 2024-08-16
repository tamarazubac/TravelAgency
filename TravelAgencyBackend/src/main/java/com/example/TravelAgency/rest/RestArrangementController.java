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

    /**
     * Retrieves all arrangements.
     *
     * @return a ResponseEntity containing a list of ArrangementDTO objects and HttpStatus.OK
     */

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArrangementDTO>> findAll(){
        List<ArrangementDTO> result= arrangementController.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieves an arrangement by its (forwarded) ID.
     *
     * @param id the ID of the arrangement to retrieve
     * @return a ResponseEntity containing the found ArrangementDTO and HttpStatus.OK, or HttpStatus.NOT_FOUND if not found
     */

    @GetMapping(value="/id/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrangementDTO> findById(@PathVariable Long id){
        Optional<ArrangementDTO> result=arrangementController.findById(id);
        if(result.isPresent()){
            return new ResponseEntity<>(result.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Creates a new arrangement.
     *
     * @param newArrangement the DTO representing the new arrangement
     * @return a ResponseEntity containing the created Arrangement and HttpStatus.CREATED, or HttpStatus.BAD_REQUEST if creation fails
     */

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SALESMAN','ADMIN')")
    public ResponseEntity<Arrangement>  create(@RequestBody ArrangementDTO newArrangement){
        Optional<Arrangement> result = arrangementController.create(newArrangement);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes an arrangement by its ID.
     *
     * @param id the ID of the arrangement to delete
     * @return a ResponseEntity with HttpStatus.NO_CONTENT
     */

    @DeleteMapping(value="/{id}")
    @PreAuthorize("hasAnyAuthority('SALESMAN','ADMIN')")
    public ResponseEntity<Arrangement> delete(@PathVariable Long id){
        arrangementController.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Updates an existing arrangement.
     *
     * @param arrangement the DTO representing the updated arrangement
     * @param id          the ID of the arrangement to update
     * @return a ResponseEntity containing the updated Arrangement and HttpStatus.OK, or HttpStatus.BAD_REQUEST if the update fails
     */


    @PutMapping(value="/{id}")
    @PreAuthorize("hasAnyAuthority('SALESMAN','ADMIN')")
    public ResponseEntity<Arrangement> update(@RequestBody ArrangementDTO arrangement, @PathVariable Long id){
        Optional<Arrangement> result = arrangementController.update(arrangement, id);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Retrieves arrangements by destination and date range.
     *
     * @param dateFrom      the start date of the range
     * @param dateTo        the end date of the range
     * @param destinationId the ID of the destination
     * @return a ResponseEntity containing a list of ArrangementDTO objects and HttpStatus.OK
     */

    @GetMapping(value="/dates/destination/{dateFrom}/{dateTo}/{destinationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArrangementDTO>> findByDestinationAndDates(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFrom,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateTo,
            @PathVariable Long destinationId) {
        List<ArrangementDTO> result = arrangementController.findByDestinationAndDates(dateFrom, dateTo, destinationId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieves arrangements by date range.
     *
     * @param dateFrom the start date of the range
     * @param dateTo   the end date of the range
     * @return a ResponseEntity containing a list of ArrangementDTO objects and HttpStatus.OK
     */

    @GetMapping(value="/dates/{dateFrom}/{dateTo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArrangementDTO>> findByDates(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFrom,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateTo
            ) {
        List<ArrangementDTO> result = arrangementController.findByDates(dateFrom, dateTo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieves arrangements by destination ID.
     *
     * @param destinationId the ID of the destination
     * @return a ResponseEntity containing a list of ArrangementDTO objects and HttpStatus.OK
     */

    @GetMapping(value="/destination/{destinationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArrangementDTO>> findByDestination( @PathVariable Long destinationId) {
        List<ArrangementDTO> result = arrangementController.findByDestination(destinationId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
