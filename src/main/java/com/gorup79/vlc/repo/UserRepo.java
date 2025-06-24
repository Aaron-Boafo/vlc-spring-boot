package com.gorup79.vlc.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gorup79.vlc.model.Users;

@Repository
public interface UserRepo extends MongoRepository<Users, Integer> {
    Users findByPhoneNumber(String phoneNumber);
    Users findById(String id);
}