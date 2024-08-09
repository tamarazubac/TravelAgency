package com.example.TravelAgency.repositories;

import com.example.TravelAgency.models.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IDestinationRepository extends JpaRepository<Destination,Long> {

    Optional<Destination> findByCityName(String city);

}
