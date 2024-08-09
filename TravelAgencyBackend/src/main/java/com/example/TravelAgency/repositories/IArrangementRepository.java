package com.example.TravelAgency.repositories;

import com.example.TravelAgency.models.Arrangement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IArrangementRepository extends JpaRepository<Arrangement,Long> {

    @Query("SELECT a FROM Arrangement a WHERE a.destination.id = :destinationId AND a.dateFrom >= :dateFrom AND a.dateTo <= :dateTo")
    List<Arrangement> findByDestinationAndDates(@Param("dateFrom") Date dateFrom,
                                                @Param("dateTo") Date dateTo,
                                                @Param("destinationId") Long destinationId);

    @Query("SELECT a FROM Arrangement a WHERE a.dateFrom >= :dateFrom AND a.dateTo <= :dateTo")
    List<Arrangement> findByDates(@Param("dateFrom") Date dateFrom,
                                                @Param("dateTo") Date dateTo);


    List<Arrangement> findByDestinationId(Long destinationId);
}
