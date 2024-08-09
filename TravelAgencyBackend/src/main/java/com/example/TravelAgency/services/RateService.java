package com.example.TravelAgency.services;

import com.example.TravelAgency.models.Rate;
import com.example.TravelAgency.repositories.IRateRepository;
import com.example.TravelAgency.services.interfaces.IRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RateService implements IRateService {
    @Autowired
    public IRateRepository rateRepository;

    @Override
    public Optional<Rate> findById(Long id) {
        return rateRepository.findById(id);
    }

    @Override
    public List<Rate> findAll() {
        return rateRepository.findAll();
    }

    @Override
    public Optional<Rate> create(Rate newRate) {
        return Optional.of(rateRepository.save(newRate));
    }

    @Override
    public Optional<Rate> update(Rate rate) {
        if(rateRepository.existsById(rate.id)){
            return Optional.of(rateRepository.saveAndFlush(rate));
        }
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {
        rateRepository.deleteById(id);
    }

    @Override
    public List<Rate> findByArrangementId(Long id) {
        return rateRepository.findByArrangementId(id);
    }
}
