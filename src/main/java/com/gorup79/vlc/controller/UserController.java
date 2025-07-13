package com.gorup79.vlc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gorup79.vlc.dto.JwtDTO;
import com.gorup79.vlc.dto.LoginDTO;
import com.gorup79.vlc.model.Users;
import com.gorup79.vlc.response.RegisterResponse;
import com.gorup79.vlc.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse<Users>> register(@Valid @RequestBody LoginDTO user,
            BindingResult bindingResult) {

        // Validate the user input
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(new RegisterResponse<>(false, errorMsg, null));
        }

        // Check if phone number already exists
        if (service.existsByPhoneNumber(user.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(new RegisterResponse<>(false, "Phone number already exists", null));
        }

        // Register the user
        Users registeredUser = service.register(user);

        if (registeredUser == null) {
            return ResponseEntity.badRequest().body(new RegisterResponse<>(false, "An internal error occured", null));

        }
        return ResponseEntity.ok(new RegisterResponse<>(true, "User registered successfully", registeredUser));
    }

    @PostMapping("/login")
    public ResponseEntity<RegisterResponse<?>> login(@Valid @RequestBody LoginDTO user,
            BindingResult bindingResult) {

        try {

            // Validate the user input
            if (bindingResult.hasErrors()) {
                String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
                return ResponseEntity.badRequest().body(new RegisterResponse<>(false, errorMsg, null));
            }

            // Check if phone number already exists
            if (!service.existsByPhoneNumber(user.getPhoneNumber())) {
                return ResponseEntity.badRequest()
                        .body(new RegisterResponse<>(false, "Phone number doesn't exists", null));
            }

            if (service.verify(user) == null || "fail".equals(service.verify(user))) {
                return ResponseEntity.badRequest()
                        .body(new RegisterResponse<>(false, "Invalid credentials, please try again",
                                null));
            }

            String validity = service.verify(user);
            return ResponseEntity
                    .ok(new RegisterResponse<>(true, "User authenticated successfully", new JwtDTO(validity)));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RegisterResponse<>(false, e.getMessage(), null));
        }

    }

    @PostMapping("/reset-password")
    public ResponseEntity<RegisterResponse<String>> resetPassword(@Valid @RequestBody LoginDTO user,
            BindingResult bindingResult) {

        try {
            // Validate the user input
            if (bindingResult.hasErrors()) {
                String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
                return ResponseEntity.badRequest().body(new RegisterResponse<>(false, errorMsg, null));
            }

            // Check if phone number exists
            if (!service.existsByPhoneNumber(user.getPhoneNumber())) {
                return ResponseEntity.badRequest()
                        .body(new RegisterResponse<>(false, "Phone number doesn't exists", null));
            }

            String resetStatus = service.resetPassword(user);
            if (!"success".equals(resetStatus)) {
                return ResponseEntity.badRequest().body(new RegisterResponse<>(false, resetStatus, null));
            }

            return ResponseEntity.ok(new RegisterResponse<>(true, "Password reset successfully", resetStatus));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RegisterResponse<>(false, e.getMessage(), null));
        }

    }

    @GetMapping("/logout")
    public ResponseEntity<RegisterResponse<String>> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // remove thr token from the cache
            service.removeTokenFromCache(token);

            return ResponseEntity.ok(new RegisterResponse<>(true, "User logged out successfully", "Logout successful"));
        }

        return ResponseEntity.badRequest().body(new RegisterResponse<>(false, "No token provided", null));
    }

}
