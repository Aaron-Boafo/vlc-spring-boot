package com.gorup79.vlc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gorup79.vlc.dto.UserInfo;
import com.gorup79.vlc.model.Profile;
import com.gorup79.vlc.model.Users;
import com.gorup79.vlc.response.RegisterResponse;
import com.gorup79.vlc.service.ProfileServices;
import com.gorup79.vlc.service.UserService;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileServices profileService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<RegisterResponse<UserInfo>> getProfile() {

        try {
            //create an instance of profile
            UserInfo profile = profileService.getProfile();

            return ResponseEntity.ok().body(new RegisterResponse<>(true, "User profile retrieved successfully", profile));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RegisterResponse<>(false, "Failed to retrieve user profile", null));
        }
       
    }

    @GetMapping("/me")
    public String getCurrentUser() {
        return userService.getCurrentUser();
    }
    
}
