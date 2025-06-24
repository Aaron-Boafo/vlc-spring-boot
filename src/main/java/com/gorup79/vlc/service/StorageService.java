package com.gorup79.vlc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gorup79.vlc.model.Storage;
import com.gorup79.vlc.repo.StorageRepo;

@Service
public class StorageService {

    @Autowired
    private UserService userService; 

    @Autowired
    private StorageRepo storageRepo; // Assuming you have a repository to handle storage operations

    public List<Storage> getAllStorageInfo() {
        try {
            // Get the current user
            String userId = userService.getCurrentUser();

            if (userId == null) {
                return null;
            }
            // Fetch storage information for the user
            return storageRepo.findAllByUserId(userId);

        } catch (Exception e) {
            return null; // If an error occurs, return null
        }
        
    }

    public Storage getStorageInfoById(String id) {
        try {
            return storageRepo.findById(id).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

}
