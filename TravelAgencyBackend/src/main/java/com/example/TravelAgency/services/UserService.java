package com.example.TravelAgency.services;

import com.example.TravelAgency.models.*;
import com.example.TravelAgency.repositories.*;
import com.example.TravelAgency.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    public IUserRepository userRepository;
    @Autowired
    public IRoleRepository roleRepository;
    @Autowired
    public IReservationRepository reservationRepository;
    @Autowired
    public IArrangementRepository arrangementRepository;
    @Autowired
    public IRateRepository rateRepository;
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findByRole(String roleName) {

            List<User> users = userRepository.findByRolesRoleName(roleName);
            return users.stream().collect(Collectors.toList());

    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> create(User user) {

        Optional<User> exist=userRepository.findByUsername(user.username);
        if(exist.isPresent()){
            return Optional.empty();
        }
        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<User> update(User user) {
        if(userRepository.existsById(user.id)){
            return Optional.of(userRepository.saveAndFlush(user));
        }
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {

        //deleting users reservations and users rates

        List<Reservation> reservations=reservationRepository.findByUserId(id);

        List<Rate> rates= rateRepository.findByUserId(id);

        for(Reservation r : reservations){

            Arrangement arrangement=r.getArrangement();
            int currentAvailableSeats=arrangement.freeSeats;
            arrangement.setFreeSeats(currentAvailableSeats+r.numberOfPeople);  //new available free seats
            arrangementRepository.saveAndFlush(arrangement);
            reservationRepository.deleteById(r.id);
        }

        for(Rate r:rates){
            rateRepository.deleteById(r.getId());
        }

        userRepository.deleteById(id);
    }

    @Override
    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    public Optional<User> addRoleToUser(Long userId, String roleName) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Role> roleOpt = roleRepository.findByRoleName(roleName);

        if (userOpt.isPresent() && roleOpt.isPresent()) {

            User user=userOpt.get();
            user.setRoles(new ArrayList<>());
            userRepository.saveAndFlush(user);  //empty list of roles

            Role role = roleOpt.get();
            user.getRoles().add(role);

            userRepository.saveAndFlush(user);
            return Optional.of(user);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> removeRoleFromUser(Long userId, String roleName) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Role> roleOpt = roleRepository.findByRoleName(roleName);

        if (userOpt.isPresent() && roleOpt.isPresent()) {
            User user = userOpt.get();
            Role role = roleOpt.get();

            if (user.getRoles().contains(role)) {
                user.getRoles().remove(role);
                userRepository.saveAndFlush(user);
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }


}
