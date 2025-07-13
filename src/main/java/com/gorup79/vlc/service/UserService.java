package com.gorup79.vlc.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gorup79.vlc.dto.LoginDTO;
import com.gorup79.vlc.model.Profile;
import com.gorup79.vlc.model.Users;
import com.gorup79.vlc.repo.ProfileRepo;
import com.gorup79.vlc.repo.UserRepo;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepo repo;

    @Autowired
    private ProfileRepo profile;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Transactional
    public Users register(LoginDTO user) {

        try {
            // generate an id for each user
            String uuid = UUID.randomUUID().toString();
            LocalDateTime currentTime = java.time.LocalDateTime.now();

            // create an instance of a new user
            Users DBuser = new Users();

            DBuser.setPhoneNumber(user.getPhoneNumber());
            DBuser.setPassword(encoder.encode(user.getPassword())); // encrypt the password
            DBuser.setId(uuid);
            DBuser.setCreatedAt(currentTime); // set the creation time

            // save the user
            repo.save(DBuser);

            // create a profile for the new user
            profile.save(new Profile(UUID.randomUUID().toString(), uuid, user.getPhoneNumber(), 0.00, null));

            // return the user after all is saved
            return DBuser;

        } catch (Exception e) {
            // if an error occurred return null
            return null;
        }

    }

    public String verify(LoginDTO user) {
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getPhoneNumber(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getPhoneNumber()); // Generate JWT token for the user
        } else {
            return "fail";
        }

    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return repo.findByPhoneNumber(phoneNumber) != null;
    }

    @Transactional
    public String resetPassword(LoginDTO user) {

        try {
            // Find the user by phone number
            Users dbUser = repo.findByPhoneNumber(user.getPhoneNumber());
            if (dbUser == null) {
                throw new Exception("User not found");
            }

            // turn the password into a hash
            String hashedPassword = encoder.encode(user.getPassword());

            // Update the user's password and updatedAt timestamp
            dbUser.setPassword(hashedPassword);
            dbUser.setUpdatedAt(LocalDateTime.now());

            repo.save(dbUser);

            // Return success message
            return "success";

        } catch (Exception e) {
            return e.getMessage();
        }

    }

    public String getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();

            // get the user by phone number
            Users user = repo.findByPhoneNumber(userId);

            if (user == null) {
                return null;
            }

            // return the user id for all searches
            return user.getId();

        } catch (Exception e) {
            return null; // If an error occurs, return null
        }

    }

    public Users getUserById() {
        Users user;
        try {
            user = repo.findById(getCurrentUser()); // Get the current user's phone number

            if (user == null) {
                return null;
            }

            // If user is found, return the user object
            return user;

        } catch (Exception e) {
            return null; // If an error occurs, return null
        }
    }

    public void removeTokenFromCache(String token) {
    }
}
