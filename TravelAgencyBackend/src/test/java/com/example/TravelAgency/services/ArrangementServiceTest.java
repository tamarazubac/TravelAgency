package com.example.TravelAgency.services;

import com.example.TravelAgency.models.Arrangement;
import com.example.TravelAgency.models.Rate;
import com.example.TravelAgency.models.Reservation;
import com.example.TravelAgency.repositories.IArrangementRepository;
import com.example.TravelAgency.repositories.IRateRepository;
import com.example.TravelAgency.repositories.IReservationRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ArrangementServiceTest {

    @MockBean
    private IArrangementRepository arrangementRepository;

    @MockBean
    private IRateRepository rateRepository;

    @MockBean
    private IReservationRepository reservationRepository;

    @Autowired
    private ArrangementService arrangementService;

    @Test
    @DisplayName("Should return all arrangements when findAll is called")
    public void testFindAll() {
        Arrangement arrangement1 = new Arrangement();
        Arrangement arrangement2 = new Arrangement();
        when(arrangementRepository.findAll()).thenReturn(Arrays.asList(arrangement1, arrangement2));

        List<Arrangement> results = arrangementService.findAll();

        assertThat(results).hasSize(2);
        verify(arrangementRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return an empty list when findAll is called and no arrangements exist")
    public void testFindAllEmpty() {
        when(arrangementRepository.findAll()).thenReturn(Collections.emptyList());

        List<Arrangement> results = arrangementService.findAll();

        assertThat(results).isEmpty();
        verify(arrangementRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return the arrangement with the given ID when findById is called")
    public void testFindById() {
        Long id = 1L;
        Arrangement arrangement = new Arrangement();
        when(arrangementRepository.findById(id)).thenReturn(Optional.of(arrangement));

        Optional<Arrangement> result = arrangementService.findById(id);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(arrangement);
        verify(arrangementRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should return empty Optional when findById is called with non-existing ID")
    public void testFindByIdNotFound() {
        Long id = 1L;
        when(arrangementRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Arrangement> result = arrangementService.findById(id);

        assertThat(result).isEmpty();
        verify(arrangementRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should create and return a new arrangement when create is called")
    public void testCreate() {
        Arrangement arrangement = new Arrangement();
        when(arrangementRepository.save(any(Arrangement.class))).thenReturn(arrangement);

        Optional<Arrangement> result = arrangementService.create(arrangement);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(arrangement);
        verify(arrangementRepository, times(1)).save(arrangement);
    }

    @Test
    @DisplayName("Should update and return the arrangement when update is called with an existing arrangement")
    public void testUpdate() {
        Long id = 1L;
        Arrangement arrangement = new Arrangement();
        arrangement.setId(id);
        when(arrangementRepository.findById(id)).thenReturn(Optional.of(arrangement));
        when(arrangementRepository.saveAndFlush(any(Arrangement.class))).thenReturn(arrangement);

        Optional<Arrangement> result = arrangementService.update(arrangement);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(arrangement);
        verify(arrangementRepository, times(1)).findById(id);
        verify(arrangementRepository, times(1)).saveAndFlush(arrangement);
    }

    @Test
    @DisplayName("Should return empty Optional when update is called with a non-existing arrangement")
    public void testUpdateNotFound() {
        Long id = 1L;
        Arrangement arrangement = new Arrangement();
        arrangement.setId(id);
        when(arrangementRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Arrangement> result = arrangementService.update(arrangement);

        assertThat(result).isEmpty();
        verify(arrangementRepository, times(1)).findById(id);
        verify(arrangementRepository, never()).saveAndFlush(any(Arrangement.class));
    }

    @Test
    @DisplayName("Should return arrangements that match the given destination and date range when findByDestinationAndDates is called")
    public void testFindByDestinationAndDates() {
        Date dateFrom = new Date();
        Date dateTo = new Date();
        Long destinationId = 1L;
        Arrangement arrangement = new Arrangement();
        when(arrangementRepository.findByDestinationAndDates(dateFrom, dateTo, destinationId))
                .thenReturn(Arrays.asList(arrangement));

        List<Arrangement> results = arrangementService.findByDestinationAndDates(dateFrom, dateTo, destinationId);

        assertThat(results).hasSize(1);
        assertThat(results.get(0)).isEqualTo(arrangement);
        verify(arrangementRepository, times(1)).findByDestinationAndDates(dateFrom, dateTo, destinationId);
    }

    @Test
    @DisplayName("Should return an empty list when findByDestinationAndDates is called with no matching arrangements")
    public void testFindByDestinationAndDatesNotFound() {
        Date dateFrom = new Date();
        Date dateTo = new Date();
        Long destinationId = 1L;
        when(arrangementRepository.findByDestinationAndDates(dateFrom, dateTo, destinationId))
                .thenReturn(Collections.emptyList());

        List<Arrangement> results = arrangementService.findByDestinationAndDates(dateFrom, dateTo, destinationId);

        assertThat(results).isEmpty();
        verify(arrangementRepository, times(1)).findByDestinationAndDates(dateFrom, dateTo, destinationId);
    }

    @Test
    @DisplayName("Should return arrangements that match the given date range when findByDates is called")
    public void testFindByDates() {
        Date dateFrom = new Date();
        Date dateTo = new Date();
        Arrangement arrangement = new Arrangement();
        when(arrangementRepository.findByDates(dateFrom, dateTo)).thenReturn(Arrays.asList(arrangement));

        List<Arrangement> results = arrangementService.findByDates(dateFrom, dateTo);

        assertThat(results).hasSize(1);
        assertThat(results.get(0)).isEqualTo(arrangement);
        verify(arrangementRepository, times(1)).findByDates(dateFrom, dateTo);
    }

    @Test
    @DisplayName("Should return an empty list when findByDates is called with no matching arrangements")
    public void testFindByDatesNotFound() {
        Date dateFrom = new Date();
        Date dateTo = new Date();
        when(arrangementRepository.findByDates(dateFrom, dateTo)).thenReturn(Collections.emptyList());

        List<Arrangement> results = arrangementService.findByDates(dateFrom, dateTo);

        assertThat(results).isEmpty();
        verify(arrangementRepository, times(1)).findByDates(dateFrom, dateTo);
    }

    @Test
    @DisplayName("Should return arrangements that match the given destination ID when findByDestination is called")
    public void testFindByDestination() {
        Long destinationId = 1L;
        Arrangement arrangement = new Arrangement();
        when(arrangementRepository.findByDestinationId(destinationId)).thenReturn(Arrays.asList(arrangement));

        List<Arrangement> results = arrangementService.findByDestination(destinationId);

        assertThat(results).hasSize(1);
        assertThat(results.get(0)).isEqualTo(arrangement);
        verify(arrangementRepository, times(1)).findByDestinationId(destinationId);
    }

    @Test
    @DisplayName("Should return an empty list when findByDestination is called with no matching arrangements")
    public void testFindByDestinationNotFound() {
        Long destinationId = 1L;
        when(arrangementRepository.findByDestinationId(destinationId)).thenReturn(Collections.emptyList());

        List<Arrangement> results = arrangementService.findByDestination(destinationId);

        assertThat(results).isEmpty();
        verify(arrangementRepository, times(1)).findByDestinationId(destinationId);
    }

    @Test
    @Transactional
    void testDeleteArrangementWithExistingRecords() {
        Long arrangementId = 1L;
        Arrangement arrangement = new Arrangement();
        Reservation reservation = new Reservation();
        reservation.setId(2L);
        Rate rate = new Rate();
        rate.setId(3L);

        when(arrangementRepository.findById(arrangementId)).thenReturn(Optional.of(arrangement));
        when(reservationRepository.findByArrangementId(arrangementId)).thenReturn(List.of(reservation));
        when(rateRepository.findByArrangementId(arrangementId)).thenReturn(List.of(rate));

        arrangementService.delete(arrangementId);

        verify(reservationRepository).deleteById(reservation.getId());
        verify(rateRepository).deleteById(rate.getId());
        verify(arrangementRepository).deleteById(arrangementId);
    }

    @Test
    @Transactional
    void testDeleteArrangementWithNoReservationsOrRates() {

        Long arrangementId = 1L;
        Arrangement arrangement = new Arrangement();

        when(arrangementRepository.findById(arrangementId)).thenReturn(Optional.of(arrangement));
        when(reservationRepository.findByArrangementId(arrangementId)).thenReturn(List.of());
        when(rateRepository.findByArrangementId(arrangementId)).thenReturn(List.of());

        arrangementService.delete(arrangementId);

        verify(reservationRepository, never()).deleteById(anyLong());
        verify(rateRepository, never()).deleteById(anyLong());
        verify(arrangementRepository).deleteById(arrangementId);
    }

    @Test
    @Transactional
    void testDeleteArrangementNotFound() {

        Long arrangementId = 1L;

        when(arrangementRepository.findById(arrangementId)).thenReturn(Optional.empty());

        arrangementService.delete(arrangementId);

        verify(reservationRepository, never()).findByArrangementId(anyLong());
        verify(rateRepository, never()).findByArrangementId(anyLong());
        verify(reservationRepository, never()).deleteById(anyLong());
        verify(rateRepository, never()).deleteById(anyLong());
        verify(arrangementRepository, never()).deleteById(anyLong());
    }

}
