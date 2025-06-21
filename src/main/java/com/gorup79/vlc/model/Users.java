package com.gorup79.vlc.model;

import org.springframework.data.mongodb.core.mapping.Document;

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

    private int id;
    private String phoneNumber;
    private String password;

    
}
