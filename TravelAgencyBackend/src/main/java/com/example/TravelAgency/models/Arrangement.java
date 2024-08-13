package com.example.TravelAgency.models;

import com.example.TravelAgency.dtos.ArrangementDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Entity
@Table(name="arrangements")
@NonNull
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Arrangement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Date dateFrom;
    public Date dateTo;
    public String description;
    public int freeSeats;
    public double pricePerPerson;

    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    public Destination destination;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User owner;

}
