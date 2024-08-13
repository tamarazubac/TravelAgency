package com.example.TravelAgency.rest;
import com.example.TravelAgency.controllers.DestinationController;
import com.example.TravelAgency.dtos.DestinationDTO;
import com.example.TravelAgency.models.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/destinations")
@CrossOrigin(origins = "http://localhost:4200")
public class RestDestinationController {

    @Value("${image.upload-dir}")
    private String imageUploadDir;

    @Autowired
    public DestinationController destinationController;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DestinationDTO>> findAll(){
        List<DestinationDTO> result= destinationController.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value="/id/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DestinationDTO> findById(@PathVariable Long id){
        Optional<DestinationDTO> result=destinationController.findById(id);
        if(result.isPresent()){
            return new ResponseEntity<>(result.get(),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Destination>  create(@RequestBody DestinationDTO newDestination){
        Optional<Destination> result = destinationController.create(newDestination);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Destination> update(@RequestBody DestinationDTO arrangement, @PathVariable Long id){
        Optional<Destination> result = destinationController.update(arrangement, id);
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Destination> delete(@PathVariable Long id){
        destinationController.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    @PostMapping("/{id}/upload-image")
    public ResponseEntity<Void> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            destinationController.uploadImage(id, file);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<List<String>> getImages(@PathVariable Long id) {
        List<String> imageNames = destinationController.getImages(id);
        List<String> imageUrls = imageNames.stream()
                .map(name -> MvcUriComponentsBuilder
                        .fromMethodName(RestDestinationController.class, "serveFile", name)
                        .build()
                        .toUriString())
                .collect(Collectors.toList());
        return new ResponseEntity<>(imageUrls, HttpStatus.OK);
    }


    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            // Resolve the file path
            Path file = Paths.get(imageUploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());

            // Check if the file exists and is readable
            if (resource.exists() && resource.isReadable()) {
                // Return the file with its content type
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(Files.probeContentType(file)))
                        .body(resource);
            } else {
                // File not found or not readable
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            // Handle malformed URL exception
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            // Handle I/O exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
