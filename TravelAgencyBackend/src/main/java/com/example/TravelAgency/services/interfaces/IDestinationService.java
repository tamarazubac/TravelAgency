package com.example.TravelAgency.services.interfaces;

import com.example.TravelAgency.models.Destination;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IDestinationService {
    Optional<Destination> findById(Long id);

    List<Destination> findAll();

    Optional<Destination> create(Destination newDestination);

    Optional<Destination> update(Destination destination);

    void delete(Long id);

    void uploadImage(Long destinationId, MultipartFile file) throws IOException;
    List<String> getImages(Long destinationId);

    void deleteImage(Long id,String fileName) throws IOException;
}
