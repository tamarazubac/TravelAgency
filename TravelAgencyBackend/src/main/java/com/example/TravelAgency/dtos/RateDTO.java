package com.example.TravelAgency.dtos;

import com.example.TravelAgency.models.Rate;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class RateDTO implements Serializable {

    private static final long serialVersionUID = -6512552723370663872L;

    public Long id;
    public String comment;
    public int rateNum;

    public UserDTO user;

    public ArrangementDTO arrangement;

    public RateDTO(Rate ent){
        this(ent,false);
    }
    public RateDTO(Rate rate, boolean includeForeignKeys) {
        id= rate.getId();
        comment=rate.getComment();
        rateNum=rate.getRateNum();

        if(includeForeignKeys){
            user=new UserDTO(rate.getUser());  //without password and roles
            arrangement=new ArrangementDTO(rate.getArrangement());   //without foreign keys
        }

    }
}
