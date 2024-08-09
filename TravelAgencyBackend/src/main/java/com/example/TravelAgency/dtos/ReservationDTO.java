package com.example.TravelAgency.dtos;


import com.example.TravelAgency.models.Reservation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@NoArgsConstructor
@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ReservationDTO implements Serializable {  //without id and full price

    private static final long serialVersionUID = -6512552723370663872L;

    public Long id;

    public int number_of_people;

    public double full_price;

    public ArrangementDTO arrangement;

    public UserDTO user;

    public ReservationDTO(Reservation ent){
        this(ent,false);
    }



    public ReservationDTO(Reservation reservation, boolean includeForeignKeys) {
        this.id=reservation.id;
        this.number_of_people=reservation.numberOfPeople;
        this.full_price=reservation.fullPrice;
        if(includeForeignKeys) {
            this.arrangement = new ArrangementDTO(reservation.arrangement,true);  //without foreign keys
            this.user = new UserDTO(reservation.user);  //without password and roles
        }

    }

}
