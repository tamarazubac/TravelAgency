package com.example.TravelAgency.dtos;

import com.example.TravelAgency.models.Destination;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DestinationDTO implements Serializable {

    private static final long serialVersionUID = -6512552723370663872L;

    public Long id;

    public String city_name;
    public String country_name;

    public DestinationDTO(Destination destination) {
        id= destination.getId();
        city_name= destination.getCityName();
        country_name= destination.getCountryName();
    }
}
