package com.example.demo.services;

import com.example.demo.dao.UserRepository;
import com.example.demo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public void createUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
       userRepository.save(user);
    }
    public void updateUser(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User getUserByActivationLink(String link) {
        return userRepository.findByActivationLink(link);
    }


}
