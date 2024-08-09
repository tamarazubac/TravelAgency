package com.example.TravelAgency.services.interfaces;

import com.example.TravelAgency.models.Reservation;

import java.util.List;
import java.util.Optional;

public interface IReservationService {

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    List<Reservation> findByArrangementId(Long id);
    List<Reservation> findByUserId(Long id);

    Optional<Reservation> create(Reservation newReservation);

    Optional<Reservation> update(Reservation reservation);

    double calculatePrice(Reservation reservation);

    void delete(Long id);
}
