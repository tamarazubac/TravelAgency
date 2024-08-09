package com.example.TravelAgency.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/logOut")
@CrossOrigin(origins = "http://localhost:4200")
public class RestLogOutController {
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('ADMIN','SALESMAN','CUSTOMER')")
    public ResponseEntity<String> logout() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //current authenticated user

            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(null);  //log out
            }
            return ResponseEntity.ok("Loged out successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error during logout !");
        }
    }
}
