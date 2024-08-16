package com.example.TravelAgency.rest;

import com.example.TravelAgency.dtos.*;
import com.example.TravelAgency.models.User;
import jakarta.validation.constraints.Null;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RestArrangementControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String salesmanToken;
    private String customerToken;
    private String adminToken;

    @BeforeEach
    public void setup() {
        // Authenticate as SALESMAN
        authenticateUser("SALESMAN", "salesman", "SALESMAN");
        // Authenticate as GUEST
        authenticateUser("CUSTOMER", "customer", "CUSTOMER");
        // Authenticate as ADMIN
        authenticateUser("ADMIN", "admin", "ADMIN");
    }

    private void authenticateUser(String username, String password, String role) {
        LogInDTO loginDTO = new LogInDTO(username, password);
        HttpEntity<LogInDTO> requestEntity = new HttpEntity<>(loginDTO, new HttpHeaders());
        ResponseEntity<AuthResponseDTO> responseEntity = restTemplate.exchange(
                "/logIn",
                HttpMethod.POST,
                requestEntity,
                AuthResponseDTO.class
        );

        HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();

        String token = Objects.requireNonNull(responseEntity.getBody()).getAccessToken();
        System.out.println(token);
        if (role.equals("SALESMAN")) {
            this.salesmanToken = token;
        } else if (role.equals("CUSTOMER")) {
            this.customerToken = token;
        } else if (role.equals("ADMIN")) {
            this.adminToken = token;
        }
    }

    @Test
    @DisplayName("Find All Arrangements - /arrangements with SALESMAN Role")
    public void testFindAllArrangementsWithSalesmanRole() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + salesmanToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<List> responseEntity = restTemplate.exchange(
                "/arrangements",
                HttpMethod.GET,
                requestEntity,
                List.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<ArrangementDTO> arrangements = responseEntity.getBody();
        assertNotNull(arrangements);

    }


    @Test
    @DisplayName("Find Arrangement by ID - /arrangements/id/{id}")
    public void testFindArrangementById() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + salesmanToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<ArrangementDTO> responseEntity = restTemplate.exchange(
                "/arrangements/id/" + 2,
                HttpMethod.GET,
                requestEntity,
                ArrangementDTO.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ArrangementDTO arrangement = responseEntity.getBody();
        assertNotNull(arrangement);
    }

    @Test
    @DisplayName("Create Arrangement Salesman - /arrangements")
    public void testCreateArrangementSalesman() {
        ArrangementDTO newArrangement = new ArrangementDTO(); // Populate with valid data
        newArrangement.setDestination(new DestinationDTO(1L,"Country","City"));
        newArrangement.setDate_from(new Date());
        newArrangement.setDate_to(new Date());

        User user=new User(1L, "test","test","test","tes","test","test",null);

        newArrangement.setOwner(new UserDTO(user,false));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + salesmanToken);
        HttpEntity<ArrangementDTO> requestEntity = new HttpEntity<>(newArrangement, headers);

        ResponseEntity<ArrangementDTO> responseEntity = restTemplate.exchange(
                "/arrangements",
                HttpMethod.POST,
                requestEntity,
                ArrangementDTO.class
        );

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        ArrangementDTO createdArrangement = responseEntity.getBody();
        assertNotNull(createdArrangement);
    }

    @Test
    @DisplayName("Create Arrangement Customer - /arrangements")
    public void testCreateArrangementCustomer() {
        ArrangementDTO newArrangement = new ArrangementDTO(); // Populate with valid data
        newArrangement.setDestination(new DestinationDTO(1L,"Country","City"));
        newArrangement.setDate_from(new Date());
        newArrangement.setDate_to(new Date());

        User user=new User(1L, "test","test","test","tes","test","test",null);

        newArrangement.setOwner(new UserDTO(user,false));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + customerToken);
        HttpEntity<ArrangementDTO> requestEntity = new HttpEntity<>(newArrangement, headers);

        ResponseEntity<ArrangementDTO> responseEntity = restTemplate.exchange(
                "/arrangements",
                HttpMethod.POST,
                requestEntity,
                ArrangementDTO.class
        );

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Create ArrangementAdmin - /arrangements")
    public void testCreateArrangementAdmin() {
        ArrangementDTO newArrangement = new ArrangementDTO();
        newArrangement.setDestination(new DestinationDTO(1L,"Country","City"));
        newArrangement.setDate_from(new Date());
        newArrangement.setDate_to(new Date());

        User user=new User(1L, "test","test","test","tes","test","test",null);

        newArrangement.setOwner(new UserDTO(user,false));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<ArrangementDTO> requestEntity = new HttpEntity<>(newArrangement, headers);

        ResponseEntity<ArrangementDTO> responseEntity = restTemplate.exchange(
                "/arrangements",
                HttpMethod.POST,
                requestEntity,
                ArrangementDTO.class
        );

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        ArrangementDTO createdArrangement = responseEntity.getBody();
        assertNotNull(createdArrangement);
    }

    @Test
    @DisplayName("Delete Arrangement Salesman - /arrangements/{id}")
    public void testDeleteArrangementSalesman() {
        Long arrangementId = 1L; // Change to an actual ID you want to test

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + salesmanToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                "/arrangements/" + arrangementId,
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Delete Arrangement Admin - /arrangements/{id}")
    public void testDeleteArrangementAdmin() {
        Long arrangementId = 1L; // Change to an actual ID you want to test

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                "/arrangements/" + arrangementId,
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Delete Arrangement Customer - /arrangements/{id}")
    public void testDeleteArrangementCustomer() {
        Long arrangementId = 1L; // Change to an actual ID you want to test

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + customerToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                "/arrangements/" + arrangementId,
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Update Arrangement - /arrangements/{id}")
    public void testUpdateArrangement() {
        ArrangementDTO updatedArrangement = new ArrangementDTO();
        updatedArrangement.setDestination(new DestinationDTO(1L,"Country","City"));
        updatedArrangement.setDate_from(new Date());
        updatedArrangement.setDate_to(new Date());
        updatedArrangement.setDescription("Description");
        updatedArrangement.setFree_seats(5);
        updatedArrangement.setPrice_per_person(1000);
        updatedArrangement.setId(2L);

        User user=new User(1L, "test","test","test","tes","test","test",null);

        updatedArrangement.setOwner(new UserDTO(user,false));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + salesmanToken);
        HttpEntity<ArrangementDTO> requestEntity = new HttpEntity<>(updatedArrangement, headers);

        ResponseEntity<ArrangementDTO> responseEntity = restTemplate.exchange(
                "/arrangements/" + 2L,
                HttpMethod.PUT,
                requestEntity,
                ArrangementDTO.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ArrangementDTO arrangement = responseEntity.getBody();
        assertNotNull(arrangement);
    }


    @Test
    @DisplayName("Update Arrangement Admin - /arrangements/{id}")
    public void testUpdateArrangementAdmin() {
        ArrangementDTO updatedArrangement = new ArrangementDTO();
        updatedArrangement.setDestination(new DestinationDTO(1L,"Country","City"));
        updatedArrangement.setDate_from(new Date());
        updatedArrangement.setDate_to(new Date());
        updatedArrangement.setDescription("Description");
        updatedArrangement.setId(2L);

        User user=new User(1L, "test","test","test","tes","test","test",null);

        updatedArrangement.setOwner(new UserDTO(user,false));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + adminToken);
        HttpEntity<ArrangementDTO> requestEntity = new HttpEntity<>(updatedArrangement, headers);

        ResponseEntity<ArrangementDTO> responseEntity = restTemplate.exchange(
                "/arrangements/" + 2L,
                HttpMethod.PUT,
                requestEntity,
                ArrangementDTO.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ArrangementDTO arrangement = responseEntity.getBody();
        assertNotNull(arrangement);
    }

    @Test
    @DisplayName("Update Arrangement Customer - /arrangements/{id}")
    public void testUpdateArrangementCustomer() {
        ArrangementDTO updatedArrangement = new ArrangementDTO();
        updatedArrangement.setDestination(new DestinationDTO(1L,"Country","City"));
        updatedArrangement.setDate_from(new Date());
        updatedArrangement.setDate_to(new Date());
        updatedArrangement.setDescription("Description");
        updatedArrangement.setId(1L);

        User user=new User(1L, "test","test","test","tes","test","test",null);

        updatedArrangement.setOwner(new UserDTO(user,false));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + customerToken);
        HttpEntity<ArrangementDTO> requestEntity = new HttpEntity<>(updatedArrangement, headers);

        ResponseEntity<ArrangementDTO> responseEntity = restTemplate.exchange(
                "/arrangements/" + 1L,
                HttpMethod.PUT,
                requestEntity,
                ArrangementDTO.class
        );

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());

    }

    @Test
    @DisplayName("Find Arrangements by Destination and Dates - /arrangements/dates/destination/{dateFrom}/{dateTo}/{destinationId}")
    public void testFindByDestinationAndDates() {
        Date dateFrom = new Date();
        Date dateTo = new Date();
        Long destinationId = 1L;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDateFrom = dateFormat.format(dateFrom);
        String formattedDateTo = dateFormat.format(dateTo);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + salesmanToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<List<ArrangementDTO>> responseEntity = restTemplate.exchange(
                "/arrangements/dates/destination/" + formattedDateFrom + "/" + formattedDateTo + "/" + destinationId,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<ArrangementDTO>>() {}
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<ArrangementDTO> arrangements = responseEntity.getBody();
        assertNotNull(arrangements);
    }

    @Test
    @DisplayName("Find Arrangements by Dates - /arrangements/dates/{dateFrom}/{dateTo}")
    public void testFindByDates() {
        Date dateFrom = new Date(); // Set actual date
        Date dateTo = new Date(); // Set actual date

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDateFrom = dateFormat.format(dateFrom);
        String formattedDateTo = dateFormat.format(dateTo);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + salesmanToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<List<ArrangementDTO>> responseEntity = restTemplate.exchange(
                "/arrangements/dates/" + formattedDateFrom + "/" + formattedDateTo,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<ArrangementDTO>>() {}
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<ArrangementDTO> arrangements = responseEntity.getBody();
        assertNotNull(arrangements);
    }

    @Test
    @DisplayName("Find Arrangements by Destination - /arrangements/destination/{destinationId}")
    public void testFindByDestination() {
        Long destinationId = 1L; // Change to an actual destination ID

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + salesmanToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<List<ArrangementDTO>> responseEntity = restTemplate.exchange(
                "/arrangements/destination/" + destinationId,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<ArrangementDTO>>() {}
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<ArrangementDTO> arrangements = responseEntity.getBody();
        assertNotNull(arrangements);
    }


}
