package com.example.TravelAgency.repositories;

import com.example.TravelAgency.models.Rate;
import com.example.TravelAgency.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IReservationRepository extends JpaRepository<Reservation,Long> {

    @Query("SELECT r FROM Reservation r WHERE r.arrangement.id = :id")
    List<Reservation> findByArrangementId(@Param("id") Long id);

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :id")
    List<Reservation> findByUserId(@Param("id") Long id);

    @Query("SELECT SUM(r.numberOfPeople) FROM Reservation r JOIN r.arrangement a WHERE a.destination.id = :destinationId AND r.arrangement.dateTo < CURRENT_DATE")
    Integer countNumberOfPeopleByDestination(@Param("destinationId") Long destinationId);

    @Query("SELECT SUM(r.fullPrice) FROM Reservation r JOIN r.arrangement a WHERE a.destination.id = :destinationId AND r.arrangement.dateTo < CURRENT_DATE")
    Double calculateTotalEarnedByDestination(Long destinationId);



    void deleteByArrangementId(Long id);
}
