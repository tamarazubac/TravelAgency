package com.example.TravelAgency.rest;


import com.example.TravelAgency.config.security.jwt.JwtTokenGenerator;
import com.example.TravelAgency.dtos.AuthResponseDTO;
import com.example.TravelAgency.dtos.LogInDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logIn")
@CrossOrigin(origins = "http://localhost:4200")
public class RestLogInController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenGenerator tokenGenerator;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponseDTO> logIn(@RequestBody LogInDTO credentilas) {

        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentilas.username,credentilas.password));


        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token=tokenGenerator.generateToken(authentication);

        System.out.println("Username : " + tokenGenerator.getUsernameFromJWT(token));

        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);

    }











    }


