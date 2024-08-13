package com.example.TravelAgency.services;

import com.example.TravelAgency.models.Destination;
import com.example.TravelAgency.repositories.IDestinationRepository;
import com.example.TravelAgency.services.interfaces.IDestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DestinationService implements IDestinationService {
    @Autowired
    public IDestinationRepository destinationRepository;

    private final String IMAGE_DIR = "images/";
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



    public void uploadImage(Long destinationId, MultipartFile file) throws IOException {
        System.out.println("Id : "+destinationId);
        Optional<Destination> destinationOpt = destinationRepository.findById(destinationId);

        if (destinationOpt.isPresent()) {
            Destination destination = destinationOpt.get();

            // unique file name
            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            Path filePath = Paths.get(IMAGE_DIR + fileName);

            System.out.println(filePath);

            // saving file to the file system
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            destination.getImagePaths().add(fileName);
            destinationRepository.save(destination);
        }
    }

    public List<String> getImages(Long destinationId) {
        Optional<Destination> destinationOpt = destinationRepository.findById(destinationId);
        return destinationOpt.map(Destination::getImagePaths).orElse(Collections.emptyList());
    }

}
