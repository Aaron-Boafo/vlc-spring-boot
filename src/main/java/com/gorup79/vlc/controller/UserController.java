package com.gorup79.vlc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gorup79.vlc.model.Users;
import com.gorup79.vlc.response.RegisterResponse;
import com.gorup79.vlc.service.UserService;
// import com.gorup79.vlc.util.OtpUtil;

import jakarta.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    // @Autowired
    // private OtpUtil otpGenerator;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse<Users>> register(@Valid @RequestBody Users user,
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
        return ResponseEntity.ok(new RegisterResponse<>(true, "User registered successfully", registeredUser));
    }



    @PostMapping("/login")
    public String login(@RequestBody Users user) {
        return service.verify(user);
    }
}
