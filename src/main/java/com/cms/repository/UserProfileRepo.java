package com.cms.repository;

import com.cms.entity.Post;
import com.cms.entity.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserProfileRepo extends MongoRepository<UserProfile, String> {
    UserProfile  findUserProfileByUserId(String userId);
}
