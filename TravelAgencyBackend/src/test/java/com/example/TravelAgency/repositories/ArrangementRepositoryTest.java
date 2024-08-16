package com.example.TravelAgency.repositories;

import com.example.TravelAgency.models.Arrangement;
import com.example.TravelAgency.models.Destination;
import com.example.TravelAgency.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ArrangementRepositoryTest {

    @Autowired
    private IArrangementRepository arrangementRepository;

    @Test
    public void testCreateAndFindById() {
        // Arrange
        Destination destination = new Destination(1L, "Paris", "France",null);
        User owner = new User(1L, "John", "Doe", "johndoe", "john.doe@example.com", "123-456-7890", "password123",null);

        Arrangement arrangement = new Arrangement();
        arrangement.setDateFrom(new Date());
        arrangement.setDateTo(new Date());
        arrangement.setDescription("Christmas in Paris");
        arrangement.setFreeSeats(10);
        arrangement.setPricePerPerson(500.00);
        arrangement.setDestination(destination);
        arrangement.setOwner(owner);

        // Act
        Arrangement savedArrangement = arrangementRepository.save(arrangement);

        // Assert
        Optional<Arrangement> foundArrangement = arrangementRepository.findById(savedArrangement.getId());

        assertThat(foundArrangement).isPresent();
        assertThat(foundArrangement.get()).isEqualToComparingFieldByField(savedArrangement);
    }

    @Test
    public void testUpdateArrangement() {
        // Arrange
        Destination destination = new Destination(1L, "Paris", "France",null);
        User owner = new User(1L, "John", "Doe", "johndoe", "john.doe@example.com", "123-456-7890", "password123",null);

        Arrangement arrangement = new Arrangement();
        arrangement.setDateFrom(new Date());
        arrangement.setDateTo(new Date());
        arrangement.setDescription("Christmas in Paris");
        arrangement.setFreeSeats(10);
        arrangement.setPricePerPerson(500.00);
        arrangement.setDestination(destination);
        arrangement.setOwner(owner);

        Arrangement savedArrangement = arrangementRepository.save(arrangement);

        // Act
        savedArrangement.setDescription("Updated Description");
        Arrangement updatedArrangement = arrangementRepository.save(savedArrangement);

        // Assert
        Optional<Arrangement> foundArrangement = arrangementRepository.findById(updatedArrangement.getId());
        assertThat(foundArrangement).isPresent();
        assertThat(foundArrangement.get().getDescription()).isEqualTo("Updated Description");
    }

    @Test
    public void testDeleteArrangement() {
        // Arrange
        Destination destination = new Destination(1L, "Paris", "France",null);
        User owner = new User(1L, "John", "Doe", "johndoe", "john.doe@example.com", "123-456-7890", "password123",null);

        Arrangement arrangement = new Arrangement();
        arrangement.setDateFrom(new Date());
        arrangement.setDateTo(new Date());
        arrangement.setDescription("Christmas in Paris");
        arrangement.setFreeSeats(10);
        arrangement.setPricePerPerson(500.00);
        arrangement.setDestination(destination);
        arrangement.setOwner(owner);

        Arrangement savedArrangement = arrangementRepository.save(arrangement);

        // Act
        arrangementRepository.deleteById(savedArrangement.getId());

        // Assert
        Optional<Arrangement> foundArrangement = arrangementRepository.findById(savedArrangement.getId());
        assertThat(foundArrangement).isNotPresent();
    }

    @Test
    public void testFindByDestinationAndDates() {
        // Arrange
        Destination destination = new Destination(1L, "Paris", "France", null);
        User owner = new User(1L, "John", "Doe", "johndoe", "john.doe@example.com", "123-456-7890", "password123", null);

        long oneDayMillis = 86400000L; // 1 day

        Date dateFrom = new Date(System.currentTimeMillis() - oneDayMillis); // 1 day before
        Date dateTo = new Date(System.currentTimeMillis() + oneDayMillis); // 1 day later


        Arrangement arrangement1 = new Arrangement();
        arrangement1.setDateFrom(dateFrom);
        arrangement1.setDateTo(dateTo);
        arrangement1.setDescription("Christmas in Paris");
        arrangement1.setFreeSeats(10);
        arrangement1.setPricePerPerson(500.00);
        arrangement1.setDestination(destination);
        arrangement1.setOwner(owner);


        Arrangement arrangement2 = new Arrangement();
        arrangement2.setDateFrom(new Date(System.currentTimeMillis() - (2 * oneDayMillis))); // 2 days before
        arrangement2.setDateTo(new Date(System.currentTimeMillis() - oneDayMillis)); // 1 day before
        arrangement2.setDescription("Previous Christmas in Paris");
        arrangement2.setFreeSeats(5);
        arrangement2.setPricePerPerson(400.00);
        arrangement2.setDestination(destination);
        arrangement2.setOwner(owner);

        arrangementRepository.save(arrangement1);
        arrangementRepository.save(arrangement2);

        // Act
        List<Arrangement> foundArrangements = arrangementRepository.findByDestinationAndDates(
                dateFrom,
                dateTo,
                destination.getId());

        // Assert
        assertThat(foundArrangements).hasSize(1);
        assertThat(foundArrangements.get(0).getDescription()).isEqualTo("Christmas in Paris");
    }

    @Test
    public void testFindByDates() {
        // Arrange
        Destination destination = new Destination(1L, "Paris", "France",null);
        User owner = new User(1L, "John", "Doe", "johndoe", "john.doe@example.com", "123-456-7890", "password123",null);

        long oneDayMillis = 86400000L;

        Date dateFrom = new Date(System.currentTimeMillis() - oneDayMillis);
        Date dateTo = new Date(System.currentTimeMillis() + oneDayMillis);

        Arrangement arrangement1 = new Arrangement();
        arrangement1.setDateFrom(dateFrom);
        arrangement1.setDateTo(dateTo);
        arrangement1.setDescription("Christmas in Paris");
        arrangement1.setFreeSeats(10);
        arrangement1.setPricePerPerson(500.00);
        arrangement1.setDestination(destination);
        arrangement1.setOwner(owner);

        Arrangement arrangement2 = new Arrangement();
        arrangement2.setDateFrom(new Date(System.currentTimeMillis() - (2 * oneDayMillis))); // 2 days before
        arrangement2.setDateTo(new Date(System.currentTimeMillis() - oneDayMillis)); // 1 day later
        arrangement2.setDescription("Previous Christmas in Paris");
        arrangement2.setFreeSeats(5);
        arrangement2.setPricePerPerson(400.00);
        arrangement2.setDestination(destination);
        arrangement2.setOwner(owner);

        arrangementRepository.save(arrangement1);
        arrangementRepository.save(arrangement2);

        // Act
        List<Arrangement> foundArrangements = arrangementRepository.findByDates(dateFrom, dateTo);

        // Assert
        assertThat(foundArrangements).hasSize(1);
        assertThat(foundArrangements.get(0).getDescription()).isEqualTo("Christmas in Paris");
    }
    @Test
    public void testFindByDestinationId() {
        // Arrange
        Destination destination = new Destination(1L, "Paris", "France",null);
        User owner = new User(1L, "John", "Doe", "johndoe", "john.doe@example.com", "123-456-7890", "password123",null);

        Arrangement arrangement1 = new Arrangement();
        arrangement1.setDateFrom(new Date(System.currentTimeMillis() - 86400000L)); // 1 day ago
        arrangement1.setDateTo(new Date(System.currentTimeMillis() + 86400000L)); // 1 day later
        arrangement1.setDescription("Christmas in Paris");
        arrangement1.setFreeSeats(10);
        arrangement1.setPricePerPerson(500.00);
        arrangement1.setDestination(destination);
        arrangement1.setOwner(owner);

        Arrangement arrangement2 = new Arrangement();
        arrangement2.setDateFrom(new Date(System.currentTimeMillis() - 172800000L)); // 2 days ago
        arrangement2.setDateTo(new Date(System.currentTimeMillis() - 86400000L)); // 1 day ago
        arrangement2.setDescription("Previous Christmas in Paris");
        arrangement2.setFreeSeats(5);
        arrangement2.setPricePerPerson(400.00);
        arrangement2.setDestination(destination);
        arrangement2.setOwner(owner);

        arrangementRepository.save(arrangement1);
        arrangementRepository.save(arrangement2);

        // Act
        List<Arrangement> foundArrangements = arrangementRepository.findByDestinationId(destination.getId());

        // Assert
        assertThat(foundArrangements).hasSize(3); //one in test-data.sql
        assertThat(foundArrangements.get(0).getDescription()).isEqualTo("Christmas in Paris");
        assertThat(foundArrangements.get(1).getDescription()).isEqualTo("Christmas in Paris");
        assertThat(foundArrangements.get(2).getDescription()).isEqualTo("Previous Christmas in Paris");
    }
}
