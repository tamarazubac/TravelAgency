package com.example.TravelAgency.controllers;



import com.example.TravelAgency.dtos.ArrangementDTO;
import com.example.TravelAgency.dtos.DestinationDTO;
import com.example.TravelAgency.models.Arrangement;
import com.example.TravelAgency.models.Destination;
import com.example.TravelAgency.services.interfaces.IDestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class DestinationController {

    @Autowired
    public IDestinationService destinationService;

    public List<DestinationDTO> findAll(){

        List<DestinationDTO> result=new ArrayList<>();
        List<Destination> destinations=destinationService.findAll();

        for(Destination d:destinations){
            result.add(toDTO(d));
        }

        return result;
    }

    public Optional<DestinationDTO> findById(Long id){
        return Optional.ofNullable(toDTO(destinationService.findById(id).get()));
    }

    public Optional<Destination> create(DestinationDTO destinationPostDTO){
        //from postDTO to ordinary;
        return destinationService.create(toEntity(destinationPostDTO,-1L));

    }


    public Optional<Destination> update(DestinationDTO destinationDTO, Long id){
        //from putDTO to entity
        return  destinationService.update(toEntity(destinationDTO,id));

    }

    public void delete(Long id){
        destinationService.delete(id);
    }


    public Destination toEntity(DestinationDTO destinationDTO,Long id){

        Destination result=new Destination();
        result.setCityName(destinationDTO.city_name);
        result.setCountryName(destinationDTO.country_name);

        if(id!=-1L){
            result.id=id;  //set id
        }

        return result;
    }

    public DestinationDTO toDTO(Destination destination){
        return new DestinationDTO(destination);
    }


    public void uploadImage(Long id, MultipartFile file) throws IOException {

        destinationService.uploadImage(id,file);

    }

    public List<String> getImages(Long id){
        return destinationService.getImages(id);
    }

}
