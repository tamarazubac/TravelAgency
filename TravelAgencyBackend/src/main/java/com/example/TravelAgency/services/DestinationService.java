package com.example.TravelAgency.services;

import com.example.TravelAgency.models.Destination;
import com.example.TravelAgency.repositories.IDestinationRepository;
import com.example.TravelAgency.services.interfaces.IDestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DestinationService implements IDestinationService {
    @Autowired
    public IDestinationRepository destinationRepository;
    @Override
    public Optional<Destination> findById(Long id) {
        return destinationRepository.findById(id);
    }

    @Override
    public List<Destination> findAll() {
        return destinationRepository.findAll();
    }

    @Override
    public Optional<Destination> create(Destination newDestination) {
        //check if location with this city name already exists

        Optional<Destination> destination=destinationRepository.findByCityName(newDestination.cityName);

        if(destination.isPresent()){
            return Optional.empty();
        }

        return Optional.of(destinationRepository.save(newDestination));
    }

    @Override
    public Optional<Destination> update(Destination destination) {
        if(destinationRepository.existsById(destination.id)){
            return Optional.of(destinationRepository.saveAndFlush(destination));
        }
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {
        destinationRepository.deleteById(id);
    }

}
