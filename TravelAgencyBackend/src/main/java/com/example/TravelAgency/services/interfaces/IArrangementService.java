package com.example.TravelAgency.services.interfaces;

import com.example.TravelAgency.models.Arrangement;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IArrangementService {

    List<Arrangement> findAll();
    Optional<Arrangement> findById(Long id);

    Optional<Arrangement> create(Arrangement arrangement);

    Optional<Arrangement> update(Arrangement arrangement);

    List<Arrangement> findByDestinationAndDates(Date dateFrom, Date dateTo, Long destinationId);

    List<Arrangement> findByDates(Date dateFrom, Date dateTo);

    List<Arrangement> findByDestination(Long destinationId);


    void delete(Long id);


}
