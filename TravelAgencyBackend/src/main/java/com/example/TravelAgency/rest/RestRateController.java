package com.example.TravelAgency.rest;

import com.example.TravelAgency.controllers.RateController;
import com.example.TravelAgency.dtos.RateDTO;
import com.example.TravelAgency.models.Rate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rates")
@CrossOrigin(origins = "http://localhost:4200")
public class RestRateController {

    @Autowired
    public RateController rateController;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RateDTO>> findAll(){
        List<RateDTO> result= rateController.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value="/id/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RateDTO> findById(@PathVariable Long id){
        Optional<RateDTO> result=rateController.findById(id);
        if(result.isPresent()){
            return new ResponseEntity<>(result.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Rate>  create(@RequestBody RateDTO newRate){
        Optional<Rate> result = rateController.create(newRate);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value="/{id}")
    @PreAuthorize("hasAnyAuthority('SALESMAN','ADMIN')")
    public ResponseEntity<Rate> delete(@PathVariable Long id){
        rateController.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping(value="/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Rate> update(@RequestBody RateDTO rate, @PathVariable Long id){
        Optional<Rate> result = rateController.update(rate, id);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value="/arrangement/{arrangementId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RateDTO>> findByArrangementId(@PathVariable Long arrangementId){
        List<RateDTO> result=rateController.findByArrangementId(arrangementId);

        return new ResponseEntity<>(result,HttpStatus.OK);

    }


}
