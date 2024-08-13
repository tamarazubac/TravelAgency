package com.example.TravelAgency.models;

import com.example.TravelAgency.dtos.DestinationDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Entity
@Table(name="destinations")
@NonNull
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String cityName;
    public String countryName;


    @ElementCollection
    @CollectionTable(name = "destination_images", joinColumns = @JoinColumn(name = "destination_id"))
    @Column(name = "image_path")
    private List<String> imagePaths;

}
