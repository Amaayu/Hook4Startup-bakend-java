package com.cms.repository;

import com.cms.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends MongoRepository<User,String> {

    User findByUsername(String username); // Corrected method name

}
