package com.example.TravelAgency.controllers;


import com.example.TravelAgency.dtos.RateDTO;
import com.example.TravelAgency.models.*;
import com.example.TravelAgency.services.interfaces.IArrangementService;
import com.example.TravelAgency.services.interfaces.IRateService;
import com.example.TravelAgency.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class RateController {

    @Autowired
    public IRateService rateService;

    @Autowired
    public IUserService userService;

    @Autowired
    public IArrangementService arrangementService;

    public List<RateDTO> findAll(){

        List<Rate> rates=rateService.findAll();

        List<RateDTO> result=new ArrayList<>();

        for(Rate r: rates){
            result.add(new RateDTO(r,true));
        }

        return result;
    }

    public Optional<RateDTO> findById(Long id){
        Optional<Rate> result=rateService.findById(id);
        if(result.isPresent()) {
            return Optional.of(new RateDTO(result.get(), true));
        }
        return Optional.empty();

    }

    public Optional<Rate> create(RateDTO ratePostDTO){
        //from postDTO to ordinary

        Optional<User> user=userService.findById(ratePostDTO.user.getId());
        Optional<Arrangement> arrangement=arrangementService.findById(ratePostDTO.arrangement.getId());

        if(user.isPresent() && arrangement.isPresent()){
            return  rateService.create(toEntity(ratePostDTO,user.get(),arrangement.get(),-1L));
        }

        return Optional.empty();

    }

    public Optional<Rate> update(RateDTO rateDTO, Long id){
        //from putDTO to entity
        Optional<User> user=userService.findById(rateDTO.user.getId());
        Optional<Arrangement> arrangement=arrangementService.findById(rateDTO.arrangement.getId());

        if(user.isPresent() && arrangement.isPresent()){

            return  rateService.update(toEntity(rateDTO,user.get(),arrangement.get(),id));
        }
        return Optional.empty();

    }

    public void delete(Long id){
        rateService.delete(id);
    }


    public Rate toEntity(RateDTO rateDTO, User user, Arrangement arrangement,Long id){

        Rate result=new Rate();
        result.setArrangement(arrangement);
        result.setUser(user);
        result.setComment(rateDTO.comment);
        result.setRateNum(rateDTO.getRateNum());

        if(id!=-1L){
            result.id=id;  //set id

        }
        return result;
    }

    public List<RateDTO> findByArrangementId(Long id){
        List<Rate> rates=rateService.findByArrangementId(id);

        List<RateDTO> result=new ArrayList<>();

        for(Rate r: rates){
            result.add(new RateDTO(r,true));
        }

        return result;

    }


}
