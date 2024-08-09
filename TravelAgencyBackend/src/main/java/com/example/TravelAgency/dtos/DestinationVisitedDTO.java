package com.example.TravelAgency.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DestinationVisitedDTO {
    private String cityName;
    private String countryName;
    private int totalPeople;
}
