package com.example.TravelAgency.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.example.TravelAgency.dtos.ArrangementDTO;
import com.example.TravelAgency.dtos.DestinationDTO;
import com.example.TravelAgency.dtos.UserDTO;
import com.example.TravelAgency.models.Arrangement;
import com.example.TravelAgency.models.Destination;
import com.example.TravelAgency.models.Role;
import com.example.TravelAgency.models.User;
import com.example.TravelAgency.services.interfaces.IDestinationService;
import com.example.TravelAgency.services.interfaces.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ArrangementMapperTest {

    @InjectMocks
    private ArrangementController arrangementMapper;

    @Mock
    private User user;

    @Mock
    private Destination destination;

    @Mock
    private IUserService userService;
    @Mock
    private IDestinationService destinationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToDTOWithoutForeignKeys() {
        // Arrange
        Arrangement arrangement = new Arrangement();
        arrangement.setId(1L);
        arrangement.setDateFrom(new Date());
        arrangement.setDateTo(new Date());
        arrangement.setDescription("Test Description");
        arrangement.setFreeSeats(5);
        arrangement.setPricePerPerson(100.0);
        arrangement.setDestination(destination);
        arrangement.setOwner(user);
        // Act
        ArrangementDTO dto = arrangementMapper.toDTO(arrangement, false);
        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDate_from()).isEqualTo(arrangement.getDateFrom());
        assertThat(dto.getDate_to()).isEqualTo(arrangement.getDateTo());
        assertThat(dto.getDescription()).isEqualTo("Test Description");
        assertThat(dto.getFree_seats()).isEqualTo(5);
        assertThat(dto.getPrice_per_person()).isEqualTo(100.0);
        // foreign keys value - null
        assertThat(dto.getDestination()).isNull();
        assertThat(dto.getOwner()).isNull();
    }

    @Test
    public void testToDTOWithForeignKeys() {
        // Arrange
        Arrangement arrangement = new Arrangement();

        Role role=new Role(1L,"SALESMAN");
        List<Role> roles=new ArrayList<>();
        roles.add(role);

        arrangement.setId(1L);
        arrangement.setDateFrom(new Date());
        arrangement.setDateTo(new Date());
        arrangement.setDescription("Test Description");
        arrangement.setFreeSeats(5);
        arrangement.setPricePerPerson(100.0);
        arrangement.setDestination(new Destination(1L, "Paris", "France",null));
        arrangement.setOwner(new User(1L, "John", "Doe", "johndoe", "john.doe@example.com", "123-456-7890", "password123",roles));

        // Act
        ArrangementDTO dto = arrangementMapper.toDTO(arrangement, true);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDate_from()).isEqualTo(arrangement.getDateFrom());
        assertThat(dto.getDate_to()).isEqualTo(arrangement.getDateTo());
        assertThat(dto.getDescription()).isEqualTo("Test Description");
        assertThat(dto.getFree_seats()).isEqualTo(5);
        assertThat(dto.getPrice_per_person()).isEqualTo(100.0);

        // foreign keys are not null
        assertThat(dto.getDestination().id).isEqualTo(1L);
        assertThat(dto.getOwner().id).isEqualTo(1L);
    }

    @Test
    public void testToEntity() {  //for update
        // Arrange
        ArrangementDTO dto = new ArrangementDTO();
        dto.setDate_from(new Date());
        dto.setDate_to(new Date());
        dto.setDescription("Test Description");
        dto.setFree_seats(5);
        dto.setPrice_per_person(100.0);
        dto.setDestination(new DestinationDTO(1L, "Paris", "France"));
        dto.setOwner(new UserDTO(user));

        when(userService.findById(1L)).thenReturn(Optional.of(user));
        when(destinationService.findById(1L)).thenReturn(Optional.of(destination));

        // Act
        Arrangement entity = arrangementMapper.toEntity(dto, user, destination, 1L);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getDescription()).isEqualTo("Test Description");
        assertThat(entity.getDateFrom()).isEqualTo(dto.getDate_from());
        assertThat(entity.getDateTo()).isEqualTo(dto.getDate_to());
        assertThat(entity.getFreeSeats()).isEqualTo(dto.getFree_seats());
        assertThat(entity.getPricePerPerson()).isEqualTo(dto.getPrice_per_person());
        assertThat(entity.getOwner()).isEqualTo(user);
        assertThat(entity.getDestination()).isEqualTo(destination);
        assertThat(entity.getId()).isEqualTo(1L);
    }

    @Test
    public void testToEntityWithoutId() {  //for create
        // Arrange
        ArrangementDTO dto = new ArrangementDTO();
        dto.setDate_from(new Date());
        dto.setDate_to(new Date());
        dto.setDescription("Test Description");
        dto.setFree_seats(5);
        dto.setPrice_per_person(100.0);
        dto.setDestination(new DestinationDTO(1L, "Paris", "France"));
        dto.setOwner(new UserDTO(user));

        when(userService.findById(1L)).thenReturn(Optional.of(user));
        when(destinationService.findById(1L)).thenReturn(Optional.of(destination));

        // Act
        Arrangement entity = arrangementMapper.toEntity(dto, user, destination, -1L);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getDescription()).isEqualTo("Test Description");
        assertThat(entity.getDateFrom()).isEqualTo(dto.getDate_from());
        assertThat(entity.getDateTo()).isEqualTo(dto.getDate_to());
        assertThat(entity.getFreeSeats()).isEqualTo(dto.getFree_seats());
        assertThat(entity.getPricePerPerson()).isEqualTo(dto.getPrice_per_person());
        assertThat(entity.getOwner()).isEqualTo(user);
        assertThat(entity.getDestination()).isEqualTo(destination);
        assertThat(entity.getId()).isNull(); // id is null
    }
}
