package com.gorup79.vlc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gorup79.vlc.dto.UpdateProfileDTO;
import com.gorup79.vlc.dto.UserInfo;
import com.gorup79.vlc.response.RegisterResponse;
import com.gorup79.vlc.service.ProfileServices;

@RestController
@CrossOrigin
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileServices profileService;

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

    @PostMapping("/update/name")
    public ResponseEntity<RegisterResponse<String>> updateProfile(@RequestBody UpdateProfileDTO data) {
        try {
            // Call the service to update the profile
            String result = profileService.updateProfileName(data);

            if (result == null || "Error".equals(result)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new RegisterResponse<>(false, "Failed to update Profile", null));
            }

            return ResponseEntity.ok(new RegisterResponse<>(true, "Profile updated successfully", result));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new RegisterResponse<>(false, "Failed to update Profile", null));
        }
    }

    @PostMapping("/update/picture")
    public ResponseEntity<RegisterResponse<String>> updateProfilePicture(@RequestPart MultipartFile file) {
        try {
            // Call the service to update the profile picture
            String result = profileService.updateProfilePicture(file);

            if (result == null || "Error".equals(result)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new RegisterResponse<>(false, "Failed to update Profile Picture", null));
            }

            return ResponseEntity.ok(new RegisterResponse<>(true, "Profile Picture updated successfully", result));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new RegisterResponse<>(false, "Failed to update Profile Picture", null));
        }
    }

    
}
