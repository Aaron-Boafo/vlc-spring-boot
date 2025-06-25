package com.gorup79.vlc.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gorup79.vlc.dto.FileUploadDTO;
import com.gorup79.vlc.model.Storage;
import com.gorup79.vlc.repo.StorageRepo;

@Service
public class StorageService {

    @Autowired
    private UserService userService; 

    @Autowired
    private CloudinaryServive cloud; // Assuming you have a service to handle cloud storage operations

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

    public String deleteInfoById(String id) {
        try {
            Storage data = storageRepo.findById(id).orElse(null);

            if (data == null) {
                return "Storage data not found";
            }

            boolean results = cloud.delete(data.getStorageLocation());

            if(!results){
                return "An error occured whiles deleting the data";
            }

            return "success";

        } catch (Exception e) {
            return "Error";
        }
    }

    public Storage getStorageInfoById(String id) {
        try {
            String userId = userService.getCurrentUser();

            if (userId == null) {
                return null;
            }

            return storageRepo.findById(id).orElse(null);
        } catch (Exception e) {
            return null; 
        }
    }

    public String add(FileUploadDTO metadata, MultipartFile file) {
       try {
            // Get the current user
            String userId = userService.getCurrentUser();
            Storage data = new Storage();

            if (userId == null) {
                throw new RuntimeException("User not authenticated");
            }

            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty or not provided");
            }

            data.setUserId(userId);
            data.setId(UUID.randomUUID().toString());
            data.setCreatedAt(java.time.LocalDateTime.now());
            
            if (metadata.getFileName() == null || metadata.getFileName().isEmpty()) {
                data.setFileName(file.getOriginalFilename());
            }else {
                data.setFileName(metadata.getFileName());
            }

            if (metadata.getFileType() == null || metadata.getFileType().isEmpty()) {
                data.setFileType(file.getContentType());
            } else {
                data.setFileType(metadata.getFileType());
            }

            //send the file to the storage location
            String secureUrl = cloud.uploadAndReturnUrl(file);

            if (secureUrl == null ) {
                throw new RuntimeException("File is empty or not provided");
            }

            data.setStorageLocation(secureUrl);
            
            //finally save the data
            storageRepo.save(data);
            return "success";

        } catch (RuntimeException e) {
        return "Error";
       }
    }

}
