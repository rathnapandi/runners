package com.axway.runners.service;

import com.axway.runners.User;
import com.axway.runners.repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public User getUser(String email){
        return userRepository.findByEmail(email);
    }

    public Iterable<User> findAll(){
        return userRepository.findAll();
    }

    public void deleteUser(String id){
        User user = userRepository.findById(id).get();
        userRepository.delete(user);
    }
}
