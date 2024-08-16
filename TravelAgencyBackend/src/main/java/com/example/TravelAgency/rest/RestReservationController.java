package com.example.TravelAgency.rest;

import com.example.TravelAgency.controllers.ReservationController;
import com.example.TravelAgency.dtos.ReservationDTO;
import com.example.TravelAgency.models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
@CrossOrigin(origins = "http://localhost:4200")
public class RestReservationController {

    @Autowired
    public ReservationController reservationController;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SALESMAN','ADMIN')")
    public ResponseEntity<List<ReservationDTO>> findAll(){
        List<ReservationDTO> result= reservationController.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value="/id/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDTO> findById(@PathVariable Long id){
        Optional<ReservationDTO> result=reservationController.findById(id);
        if(result.isPresent()){
            return new ResponseEntity<>(result.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Reservation>  create(@RequestBody ReservationDTO newReservation){
        Optional<Reservation> result = reservationController.create(newReservation);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Reservation> delete(@PathVariable Long id){
        reservationController.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Reservation> update(@RequestBody ReservationDTO reservation, @PathVariable Long id){
        Optional<Reservation> result = reservationController.update(reservation, id);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value="/arrangement/{arrangementId}",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SALESMAN', 'ADMIN')")
    public ResponseEntity<List<ReservationDTO>> findByArrangementId(@PathVariable Long arrangementId){
        List<ReservationDTO> result=reservationController.findByArrangementId(arrangementId);

        return new ResponseEntity<>(result,HttpStatus.OK);

    }

    @PreAuthorize("hasAnyAuthority('SALESMAN', 'CUSTOMER')")
    @GetMapping(value="/user/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReservationDTO>> findByUserId(@PathVariable Long userId){
        List<ReservationDTO> result=reservationController.findByUserId(userId);

        return new ResponseEntity<>(result,HttpStatus.OK);

    }


}
