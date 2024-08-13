package com.example.TravelAgency.repositories;

import com.example.TravelAgency.models.Arrangement;
import com.example.TravelAgency.models.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IRateRepository extends JpaRepository<Rate,Long> {

    @Query("SELECT r FROM Rate r WHERE r.arrangement.id = :id")
    List<Rate> findByArrangementId(@Param("id") Long id);

    @Query("SELECT r FROM Rate r WHERE r.user.id = :id")
    List<Rate> findByUserId(@Param("id") Long id);
    void deleteByArrangementId(Long id);
}
