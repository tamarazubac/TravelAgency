package com.example.TravelAgency.config.security;

import com.example.TravelAgency.models.Role;
import com.example.TravelAgency.models.User;
import com.example.TravelAgency.repositories.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user=userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username not found !"));  //user entity
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),mapRolesToAuthorities(user.getRoles()));  //userdetails model

    }
    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        for (Role r : roles) {
            System.out.println("Role : " + r.toString());
        }
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }

}
