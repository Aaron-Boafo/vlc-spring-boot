package com.gorup79.vlc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gorup79.vlc.dto.UpdateProfileDTO;
import com.gorup79.vlc.dto.UserInfo;
import com.gorup79.vlc.model.Profile;
import com.gorup79.vlc.model.Users;
import com.gorup79.vlc.repo.ProfileRepo;

@Service
public class ProfileServices {

    @Autowired
    private ProfileRepo repo; // to get the profile from the database

    @Autowired
    private UserService userService; //get the current user from authentication context

    public UserInfo getProfile() {
        try {
            // Get the current user's ID from authentication context
            String userId = userService.getCurrentUser();

            // Fetch user and profile by userId
            Users user = userService.getUserById();
            Profile profile = repo.findByUserId(userId);

            // Check for nulls
            if (user == null || profile == null) {
                return null;
            }

            // Build UserInfo
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId() != null ? user.getId() : null);
            userInfo.setPhoneNumber(user.getPhoneNumber() != null ? user.getPhoneNumber() : null);
            userInfo.setUsername(profile.getUsername() != null ? profile.getUsername() : null);
            userInfo.setStorageUsed(profile.getStorageUsed());
            userInfo.setProfilePicture(profile.getProfilePictureUrl() != null ? profile.getProfilePictureUrl() : null);
            userInfo.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
            userInfo.setUpdatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null);

            return userInfo;

        } catch (Exception e) {
            return null;
        }
        
    }

    @Transactional
    public String updateProfile(UpdateProfileDTO data, MultipartFile image) {
        try {
            // Get the current user's ID from authentication context
            String userId = userService.getCurrentUser();

            // Fetch profile by userId
            Profile profile = repo.findByUserId(userId);

            //save the user name in a string
            String username = data.getUsername();

            if (username == null || username.isEmpty()) {
                username = profile.getUsername();
            }
            
            // Update user and profile information
            profile.setUsername(username);

            // Save changes
            repo.save(profile);

            return "Profile updated successfully";

        } catch (Exception e) {
            return "Error";
        }
    }
    
}
