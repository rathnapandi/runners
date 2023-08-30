package com.axway.runners.service;

import com.axway.runners.model.User;
import com.axway.runners.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public User getUser(String email){
        return userRepository.findByEmail(email);
    }

    public User findByAthleteId(String athleteId){
        return userRepository.findByAthleteId(athleteId);
    }

    public Iterable<User> findAll(){
        return userRepository.findAll();
    }

    public void deleteUser(String id){

        Optional<User> user = userRepository.findById(id);
        user.ifPresent(userRepository::delete);
    }


}
