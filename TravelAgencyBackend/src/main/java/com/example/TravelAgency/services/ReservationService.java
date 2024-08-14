package com.example.TravelAgency.services;

import com.example.TravelAgency.dtos.NotificationDTO;
import com.example.TravelAgency.models.Arrangement;
import com.example.TravelAgency.models.Reservation;
import com.example.TravelAgency.repositories.IArrangementRepository;
import com.example.TravelAgency.repositories.IReservationRepository;
import com.example.TravelAgency.services.interfaces.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService implements IReservationService {

    @Autowired
    public IReservationRepository reservationRepository;
    @Autowired
    IArrangementRepository arrangementRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public List<Reservation> findByArrangementId(Long id) {
        return reservationRepository.findByArrangementId(id);
    }

    @Override
    public List<Reservation> findByUserId(Long id) {
        return reservationRepository.findByUserId(id);
    }

    @Override
    public Optional<Reservation> create(Reservation newReservation) {
        newReservation.fullPrice=calculatePrice(newReservation);


        //calculate free seats

        Optional<Arrangement> arrangement=arrangementRepository.findById(newReservation.arrangement.id);
        if(arrangement.isPresent()){
            int currentFreeSeats=arrangement.get().freeSeats;
            arrangement.get().setFreeSeats(currentFreeSeats- newReservation.numberOfPeople);
            arrangementRepository.saveAndFlush(arrangement.get());
        }

        NotificationDTO not=new NotificationDTO();
        not.setUsername(arrangement.get().owner.username);
        not.setType("RESERVATION_CREATED");
        not.setTime(LocalDateTime.now());
        not.setContent("Created reservation for your arrangement on destination: "+arrangement.get().destination.countryName.toUpperCase()+", "+arrangement.get().destination.cityName.toUpperCase()+ "  by customer "+newReservation.user.username+"!");


        this.simpMessagingTemplate.convertAndSend( "/socket-publisher/"+arrangement.get().owner.username,not);
        System.out.println("Putanjaaaa  : "+"/socket-publisher/"+arrangement.get().owner.username);

        return Optional.of(reservationRepository.save(newReservation));
    }

    @Override
    public Optional<Reservation> update(Reservation reservation) {
        reservation.fullPrice=calculatePrice(reservation);  //updating price
        if (reservationRepository.findById(reservation.id).isPresent()){
            return Optional.of(reservationRepository.saveAndFlush(reservation));
        }
        return Optional.empty();
    }

    @Override
    public double calculatePrice(Reservation reservation) {
        return reservation.numberOfPeople*reservation.arrangement.pricePerPerson;
    }

    @Override
    public void delete(Long id) {


        Optional<Reservation> reservation=reservationRepository.findById(id);

        // check if reservation's start date is in the past

        if (reservation.get().getArrangement().getDateFrom().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Cannot delete reservation as its start date is in the past.");
        }

        //calculate available seats

        if(reservation.isPresent()){
            Arrangement arrangement=reservation.get().getArrangement();
            int currentAvailableSeats=arrangement.freeSeats;
            arrangement.setFreeSeats(currentAvailableSeats+reservation.get().numberOfPeople);
            arrangementRepository.saveAndFlush(arrangement);
        }
        reservationRepository.deleteById(id);
    }
}
