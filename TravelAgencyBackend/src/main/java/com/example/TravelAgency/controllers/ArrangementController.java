package com.example.TravelAgency.controllers;
import com.example.TravelAgency.dtos.ArrangementDTO;
import com.example.TravelAgency.models.Arrangement;
import com.example.TravelAgency.models.Destination;
import com.example.TravelAgency.models.User;
import com.example.TravelAgency.services.interfaces.IDestinationService;
import com.example.TravelAgency.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.example.TravelAgency.services.interfaces.IArrangementService;

import java.util.*;

@Controller

public class ArrangementController {

    @Autowired
    public IArrangementService arrangementService;
    @Autowired
    public IUserService userService;
    @Autowired
    public IDestinationService destinationService;

    public List<ArrangementDTO> findAll(){
        List <ArrangementDTO> result = new ArrayList<>();

        for(Arrangement a : arrangementService.findAll()){
            result.add(toDTO(a,true));
        }

        return result;
    }

    public Optional<ArrangementDTO> findById(Long id){

        Optional<Arrangement> arrangement=arrangementService.findById(id);

        if(arrangement.isPresent()){
            return Optional.of(toDTO(arrangement.get(),true));
        }

        return Optional.empty();
    }

    public Optional<Arrangement> create(ArrangementDTO arrangementPostDTO){
        //from postDTO to ordinary

        Optional<User> owner=userService.findById(arrangementPostDTO.owner.id);

        Optional<Destination> destination=destinationService.findById(arrangementPostDTO.destination.id);

        if(owner.isPresent() && destination.isPresent()){
            return  arrangementService.create(toEntity(arrangementPostDTO,owner.get(),destination.get(),-1L));  //-1L for create
        }

        return Optional.empty();

    }

    public void delete(Long id){
        arrangementService.delete(id);
    }


    public Optional<Arrangement> update(ArrangementDTO arrangementPutDTO,Long id){
        //from putDTO to entity

        Optional<User> owner=userService.findById(arrangementPutDTO.owner.id);

        Optional<Destination> destination=destinationService.findById(arrangementPutDTO.destination.id);

        if(owner.isPresent() && destination.isPresent()){
            return  arrangementService.update(toEntity(arrangementPutDTO,owner.get(),destination.get(),id));
        }

        return Optional.empty();

    }

    public List<ArrangementDTO> findByDestinationAndDates(Date dateFrom,Date dateTo,Long destinationId){
        List <ArrangementDTO> result = new ArrayList<>();

        for(Arrangement a : arrangementService.findByDestinationAndDates(dateFrom,dateTo,destinationId)){
            result.add(toDTO(a,true));
        }

        return result;
    }

    public List<ArrangementDTO> findByDestination(Long destinationId){
        List <ArrangementDTO> result = new ArrayList<>();

        for(Arrangement a : arrangementService.findByDestination(destinationId)){
            result.add(toDTO(a,true));
        }

        return result;
    }

    public List<ArrangementDTO> findByDates(Date dateFrom,Date dateTo){
        List <ArrangementDTO> result = new ArrayList<>();

        for(Arrangement a : arrangementService.findByDates(dateFrom,dateTo)){
            result.add(toDTO(a,true));
        }

        return result;
    }


    public ArrangementDTO toDTO(Arrangement a,boolean includeForeignKeys){
        return new ArrangementDTO(a,includeForeignKeys);  //return entities without/with foreign keys
    }

    public Arrangement toEntity(ArrangementDTO arrangementDTO,User owner,Destination destination,Long id){

        Arrangement result=new Arrangement();

        result.setDescription(arrangementDTO.description);
        result.setDateFrom(arrangementDTO.date_from);
        result.setDateTo(arrangementDTO.date_to);
        result.setOwner(owner);
        result.setDestination(destination);
        result.setFreeSeats(arrangementDTO.free_seats);
        result.setPricePerPerson(arrangementDTO.price_per_person);

        if(id!=-1L){
            result.id=id;  //set id - for updates
        }
        return result;
    }

}
