package com.example.TravelAgency.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BalanceReportDTO {
    private Integer reservationId;
    private String destination;
    private Integer numberOfPeople;
    private Double fullPrice;
}
