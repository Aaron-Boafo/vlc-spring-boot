package com.gorup79.vlc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StorageService {

    @Autowired
    private UserService userService; //get the current user from authentication context

}
