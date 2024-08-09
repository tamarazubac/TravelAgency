package com.example.TravelAgency.controllers;

import com.example.TravelAgency.dtos.ReservationDTO;
import com.example.TravelAgency.models.*;
import com.example.TravelAgency.services.interfaces.IArrangementService;
import com.example.TravelAgency.services.interfaces.IReservationService;
import com.example.TravelAgency.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ReservationController {

    @Autowired
    public IReservationService reservationService;

    @Autowired
    public IUserService userService;

    @Autowired
    public IArrangementService arrangementService;

    public List<ReservationDTO> findAll(){

        List<Reservation> reservations=reservationService.findAll();
        List<ReservationDTO> result=new ArrayList<>();

        for(Reservation r:reservations){
            result.add(new ReservationDTO(r,true));
        }

        return result;
    }

    public Optional<ReservationDTO> findById(Long id){
        Optional<Reservation> reservation=reservationService.findById(id);
        if(reservation.isPresent()){
        ReservationDTO result=new ReservationDTO(reservation.get(),true);
        return Optional.of(result);
        }

        return Optional.empty();
    }

    public Optional<Reservation> create(ReservationDTO reservationPostDTO){
        //from postDTO to ordinary

        Optional<User> user=userService.findById(reservationPostDTO.user.id);
        Optional<Arrangement> arrangement=arrangementService.findById(reservationPostDTO.arrangement.getId());

        if(user.isPresent() && arrangement.isPresent()){
            return  reservationService.create(toEntity(reservationPostDTO,user.get(),arrangement.get(),-1L));
        }

        return Optional.empty();

    }

    public void delete(Long id){
        reservationService.delete(id);
    }

    public Optional<Reservation> update(ReservationDTO reservationDTO, Long id){
        //from putDTO to entity

        Optional<User> user=userService.findById(reservationDTO.user.getId());
        Optional<Arrangement> arrangement=arrangementService.findById(reservationDTO.arrangement.getId());


        if(user.isPresent() && arrangement.isPresent()){
            return  reservationService.update(toEntity(reservationDTO,user.get(),arrangement.get(),id));
        }
        return  Optional.empty();

    }


    public List<ReservationDTO> findByArrangementId(Long id){
        List<Reservation> rates=reservationService.findByArrangementId(id);

        List<ReservationDTO> result=new ArrayList<>();

        for(Reservation r: rates){
            result.add(new ReservationDTO(r,true));
        }

        return result;

    }

    public List<ReservationDTO> findByUserId(Long id){
        List<Reservation> rates=reservationService.findByUserId(id);

        List<ReservationDTO> result=new ArrayList<>();

        for(Reservation r: rates){
            result.add(new ReservationDTO(r,true));
        }

        return result;

    }

    public Reservation toEntity(ReservationDTO reservationDTO, User owner, Arrangement arrangement, Long id){

        Reservation result=new Reservation();

        result.setArrangement(arrangement);
        result.setUser(owner);
        result.setNumberOfPeople(reservationDTO.number_of_people);

        if(id!=-1L){
            result.id=id;   //set id

        }

        return result;
    }



}
