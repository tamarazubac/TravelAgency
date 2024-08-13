package com.example.TravelAgency.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class DestinationReportDTO {

    private String cityName;
    private String countryName;
    private int totalPeople;
    private int totalReservations;
    private double totalRevenue;


}
