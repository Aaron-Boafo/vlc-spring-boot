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
    private UserService userService; // get the current user from authentication context

    @Autowired
    CloudinaryServive cloud;

    public UserInfo getProfile() {
        // Get the current user's ID from authentication context
        String userId = userService.getCurrentUser();

        // Fetch user and profile by userId
        Users user = userService.getUserById();
        Profile profile = repo.findByUserId(userId);

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
    }

    @Transactional
    public String updateProfileName(UpdateProfileDTO data, MultipartFile profileImage) {
        try {
            String userId = userService.getCurrentUser();
            Profile profile = repo.findByUserId(userId);

            // save the user name in a string
            String username = data.getUsername();

            if (username == null || username.isEmpty()) {
                username = profile.getUsername();
            }

            profile.setUsername(username);

            // If profileImage is not null, upload it to Cloudinary
            if (profileImage != null && !profileImage.isEmpty()) {
                String secureUrl = cloud.uploadAndReturnUrl(profileImage);
                profile.setProfilePictureUrl(secureUrl);
            }

            repo.save(profile);
            return "Profile updated successfully";

        } catch (Exception e) {
            return "Error";
        }
    }

    public Profile getProfileByUserId(String userId) {
        return repo.findByUserId(userId);
    }

}
