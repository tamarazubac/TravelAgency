package com.example.TravelAgency.services.interfaces;

import com.example.TravelAgency.models.Destination;
import com.example.TravelAgency.models.Rate;

import java.util.List;
import java.util.Optional;

public interface IRateService {

    Optional<Rate> findById(Long id);

    List<Rate> findAll();

    Optional<Rate> create(Rate newRate);

    Optional<Rate> update(Rate rate);

    void delete(Long id);

    List<Rate> findByArrangementId(Long id);
}
