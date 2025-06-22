package com.gorup79.vlc.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "users")
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class Users {

    private String id; //uuid generated string

    @NotNull(message = "Phone number must not be null")
    @NotEmpty(message = "Phone number must not be empty")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phoneNumber;

    @NotNull(message = "Password must not be null")
    @NotEmpty(message = "Password must not be empty")
    @Min(value = 8, message = "Password must be at least 8 characters long")
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
}
