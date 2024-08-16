package com.example.TravelAgency.dtos;


import com.example.TravelAgency.models.Arrangement;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
@Data
public class ArrangementDTO implements Serializable {

    private static final long serialVersionUID = -6512552723370663872L;

    public Long id;
    public Date date_from;

    public Date date_to;

    public String description;

    public int free_seats;

    public double price_per_person;

    public DestinationDTO destination;

    public UserDTO owner;

    public ArrangementDTO(Arrangement ent) {
        this(ent, false);
    }


    public ArrangementDTO(Arrangement entity, boolean includeForeignKeys) {
        id= entity.getId();
        date_from=entity.getDateFrom();
        date_to=entity.getDateTo();
        description=entity.getDescription();
        free_seats=entity.getFreeSeats();
        price_per_person=entity.getPricePerPerson();

        if (includeForeignKeys){
            owner=new UserDTO(entity.getOwner(),true);
            destination=new DestinationDTO(entity.getDestination());
        }
    }
}
