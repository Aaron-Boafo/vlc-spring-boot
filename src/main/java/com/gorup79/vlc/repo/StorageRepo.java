package com.gorup79.vlc.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gorup79.vlc.model.Storage;

@Repository
public interface StorageRepo extends MongoRepository<Storage, String> {

    List<Storage> findAllByUserId(String userId);
    
}
