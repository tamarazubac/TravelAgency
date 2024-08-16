package com.example.TravelAgency.services;

import com.example.TravelAgency.models.Arrangement;
import com.example.TravelAgency.models.Rate;
import com.example.TravelAgency.models.Reservation;
import com.example.TravelAgency.repositories.IRateRepository;
import com.example.TravelAgency.repositories.IReservationRepository;
import com.example.TravelAgency.services.interfaces.IArrangementService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.TravelAgency.repositories.IArrangementRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ArrangementService implements IArrangementService {

    @Autowired
    public IArrangementRepository arrangementRepository;
    @Autowired
    public IRateRepository rateRepository;
    @Autowired
    public IReservationRepository reservationRepository;

    @Override
    public List<Arrangement> findAll() {
        return arrangementRepository.findAll();
    }

    @Override
    public Optional<Arrangement> findById(Long id) {
        return arrangementRepository.findById(id);
    }

    @Override
    public Optional<Arrangement> create(Arrangement arrangement) {
        return Optional.of(arrangementRepository.save(arrangement));
    }

    @Override
    public Optional<Arrangement> update(Arrangement arrangement) {
        if(arrangementRepository.findById(arrangement.id).isPresent()) {
            return Optional.of(arrangementRepository.saveAndFlush(arrangement));
        }
        return Optional.empty();
    }

    @Override
    public List<Arrangement> findByDestinationAndDates(Date dateFrom, Date dateTo, Long destinationId) {
        return arrangementRepository.findByDestinationAndDates(dateFrom,dateTo,destinationId);
    }

    @Override
    public List<Arrangement> findByDates(Date dateFrom, Date dateTo) {
        return arrangementRepository.findByDates(dateFrom,dateTo);
    }

    @Override
    public List<Arrangement> findByDestination(Long destinationId) {
        return arrangementRepository.findByDestinationId(destinationId);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Optional<Arrangement> arrangement=arrangementRepository.findById(id);
        if(arrangement.isPresent()) {

            List<Reservation> reservations = reservationRepository.findByArrangementId(id);
            for (Reservation reservation : reservations) {
                reservationRepository.deleteById(reservation.getId());
            }

            // Delete all rates associated with the arrangement
            List<Rate> rates = rateRepository.findByArrangementId(id);
            for (Rate rate : rates) {
                rateRepository.deleteById(rate.getId());
            }

            // Finally, delete the arrangement itself
            arrangementRepository.deleteById(id);
        }
    }
}
