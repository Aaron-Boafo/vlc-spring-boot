package com.gorup79.vlc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gorup79.vlc.dto.FileUploadDTO;
import com.gorup79.vlc.model.Storage;
import com.gorup79.vlc.response.RegisterResponse;
import com.gorup79.vlc.service.StorageService;

@RestController
@CrossOrigin
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    private StorageService service; // Assuming you have a service to handle storage operations

    @GetMapping()
    public ResponseEntity<RegisterResponse<List<?>>> getStorageInfo() {

        try {
            // get all the list of storage information
            List<Storage> storageList = service.getAllStorageInfo();

            if (storageList == null) {
                return ResponseEntity.ok(new RegisterResponse<>(false, "No storage information found.", null));
            }

            return ResponseEntity
                    .ok(new RegisterResponse<>(true, "Storage information retrieved successfully.", storageList));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new RegisterResponse<>(false, "An error occurred while checking authentication.", null));
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<RegisterResponse<Storage>> getStorageInfoById(@PathVariable String id) {

        try {
            // Get the storage information by ID
            Storage storage = service.getStorageInfoById(id);

            if (storage == null) {
                return ResponseEntity.ok(new RegisterResponse<>(false, "No storage information found.", null));
            }

            return ResponseEntity
                    .ok(new RegisterResponse<>(true, "Storage information retrieved successfully.", storage));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new RegisterResponse<>(false, "An error occurred while checking authentication.", null));
        }

    }

    @PostMapping("/add")
    public ResponseEntity<RegisterResponse<?>> addStorageInfo(@RequestPart("metadata") FileUploadDTO metadata,
            @RequestPart("file") MultipartFile file) {
        System.out.println("Adding storage information: " + metadata + ", file: " + file.getOriginalFilename());
        try {

            String results = service.add(metadata, file);

            if ("Error".equals(results)) {
                throw new Exception("Error");
            }

            // Save the storage information
            return ResponseEntity.ok(new RegisterResponse<>(true, "Storage information added successfully.", null));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new RegisterResponse<>(false, "An error occurred while checking authentication.", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RegisterResponse<?>> deleteStorage(@PathVariable String id) {

        try {
            // delete stored information by ID
            String storage = service.deleteInfoById(id);

            if (storage == null) {
                return ResponseEntity.ok(new RegisterResponse<>(false, "No storage information found.", null));
            }

            if (!"success".equals(storage)) {
                return ResponseEntity
                        .ok(new RegisterResponse<>(false, "An error occured whiles deleting the data", null));

            }

            return ResponseEntity.ok(new RegisterResponse<>(true, "Storage information retrieved successfully.", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new RegisterResponse<>(false, "An error occurred while checking authentication.", null));
        }

    }

}