package com.example.TravelAgency.models;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ArrangementModelTest {
    @Test
    public void testGettersAndSetters() {
        // Arrange
        Arrangement arrangement = new Arrangement();
        Date dateFrom = new Date();
        Date dateTo = new Date();
        String description = "Test Description";
        int freeSeats = 10;
        double pricePerPerson = 99.99;
        Destination destination = new Destination();
        User owner = new User();

        // Act
        arrangement.setDateFrom(dateFrom);
        arrangement.setDateTo(dateTo);
        arrangement.setDescription(description);
        arrangement.setFreeSeats(freeSeats);
        arrangement.setPricePerPerson(pricePerPerson);
        arrangement.setDestination(destination);
        arrangement.setOwner(owner);

        // Assert
        assertThat(arrangement.getDateFrom()).isEqualTo(dateFrom);
        assertThat(arrangement.getDateTo()).isEqualTo(dateTo);
        assertThat(arrangement.getDescription()).isEqualTo(description);
        assertThat(arrangement.getFreeSeats()).isEqualTo(freeSeats);
        assertThat(arrangement.getPricePerPerson()).isEqualTo(pricePerPerson);
        assertThat(arrangement.getDestination()).isEqualTo(destination);
        assertThat(arrangement.getOwner()).isEqualTo(owner);
    }

    @Test
    public void testToString() {
        // Arrange
        Date dateFrom = new Date();
        Date dateTo = new Date();
        String description = "Test Description";
        int freeSeats = 10;
        double pricePerPerson = 99.99;
        Destination destination = new Destination();
        User owner = new User();

        Arrangement arrangement = new Arrangement(1L, dateFrom, dateTo, description, freeSeats, pricePerPerson, destination, owner);

        // Act
        String toString = arrangement.toString();

        // Assert
        assertThat(toString).contains(description);
        assertThat(toString).contains(String.valueOf(dateFrom));
        assertThat(toString).contains(String.valueOf(dateTo));
        assertThat(toString).contains(String.valueOf(freeSeats));
        assertThat(toString).contains(String.valueOf(pricePerPerson));

    }

    @Test
    public void testConstructors() {
        // default constructor
        Arrangement arrangement = new Arrangement();
        assertThat(arrangement).isNotNull();

        // parameterized constructor
        Date dateFrom = new Date();
        Date dateTo = new Date();
        String description = "Test Description";
        int freeSeats = 10;
        double pricePerPerson = 99.99;
        Destination destination = new Destination();
        User owner = new User();

        Arrangement arrangementWithParams = new Arrangement(1L, dateFrom, dateTo, description, freeSeats, pricePerPerson, destination, owner);
        assertThat(arrangementWithParams.getId()).isEqualTo(1L);
        assertThat(arrangementWithParams.getDateFrom()).isEqualTo(dateFrom);
        assertThat(arrangementWithParams.getDateTo()).isEqualTo(dateTo);
        assertThat(arrangementWithParams.getDescription()).isEqualTo(description);
        assertThat(arrangementWithParams.getFreeSeats()).isEqualTo(freeSeats);
        assertThat(arrangementWithParams.getPricePerPerson()).isEqualTo(pricePerPerson);
        assertThat(arrangementWithParams.getDestination()).isEqualTo(destination);
        assertThat(arrangementWithParams.getOwner()).isEqualTo(owner);
    }

    @Test
    public void testEquals() {
        // Arrange
        Date dateFrom = new Date();
        Date dateTo = new Date();
        String description = "Test Description";
        int freeSeats = 10;
        double pricePerPerson = 99.99;
        Destination destination = new Destination();
        User owner = new User();

        Arrangement arrangement1 = new Arrangement(1L, dateFrom, dateTo, description, freeSeats, pricePerPerson, destination, owner);
        Arrangement arrangement2 = new Arrangement(1L, dateFrom, dateTo, description, freeSeats, pricePerPerson, destination, owner);
        Arrangement arrangement3 = new Arrangement(2L, dateFrom, dateTo, description, freeSeats, pricePerPerson, destination, owner);

        // Act & Assert
        assertThat(arrangement1).isEqualTo(arrangement2);
        assertThat(arrangement1).isNotEqualTo(arrangement3);
        assertThat(arrangement2).isNotEqualTo(null);
        assertThat(arrangement2).isNotEqualTo(new Object());
    }

    @Test
    public void testHashCode() {
        // Arrange
        Date dateFrom = new Date();
        Date dateTo = new Date();
        String description = "Test Description";
        int freeSeats = 10;
        double pricePerPerson = 99.99;
        Destination destination = new Destination();
        User owner = new User();

        Arrangement arrangement1 = new Arrangement(1L, dateFrom, dateTo, description, freeSeats, pricePerPerson, destination, owner);
        Arrangement arrangement2 = new Arrangement(1L, dateFrom, dateTo, description, freeSeats, pricePerPerson, destination, owner);
        Arrangement arrangement3 = new Arrangement(2L, dateFrom, dateTo, description, freeSeats, pricePerPerson, destination, owner);

        // Act & Assert
        assertThat(arrangement1.hashCode()).isEqualTo(arrangement2.hashCode());
        assertThat(arrangement1.hashCode()).isNotEqualTo(arrangement3.hashCode());
    }

}
