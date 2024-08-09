package com.example.TravelAgency.models;

import com.example.TravelAgency.dtos.ReservationDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name="reservations")
@NonNull
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public int numberOfPeople;
    public double fullPrice;
    @ManyToOne
    @JoinColumn(name = "arrangement_id", nullable = false)
    public Arrangement arrangement;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;
}
