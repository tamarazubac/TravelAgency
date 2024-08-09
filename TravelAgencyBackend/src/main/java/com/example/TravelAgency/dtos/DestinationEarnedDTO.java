package com.example.TravelAgency.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DestinationEarnedDTO {
    private String cityName;
    private String countryName;
    private double earnedMoney;


}
