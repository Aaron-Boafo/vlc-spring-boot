package com.gorup79.vlc.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gorup79.vlc.model.Users;
import com.gorup79.vlc.repo.UserRepo;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepo repo;


    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users user) {
        user.setPassword(encoder.encode(user.getPassword())); // encrypt the password
        user.setId(UUID.randomUUID().toString()); // change the ID generation to UUID
        user.setCreatedAt(java.time.LocalDateTime.now()); // set the creation time

        repo.save(user);
        return user;
    }

    public String verify(Users user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getPhoneNumber(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getPhoneNumber());
        } else {
            return "fail";
        }
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return repo.findByPhoneNumber(phoneNumber) != null;
    }
}
