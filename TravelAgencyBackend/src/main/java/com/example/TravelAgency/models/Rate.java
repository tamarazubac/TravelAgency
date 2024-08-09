package com.example.TravelAgency.models;

import com.example.TravelAgency.dtos.RateDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name="rates")
@NonNull
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String comment;
    public int rateNum;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    public User user;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "arrangement_id", nullable = false)
    public Arrangement arrangement;

}
