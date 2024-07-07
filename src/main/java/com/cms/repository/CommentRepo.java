package com.cms.repository;

import com.cms.entity.Comment;
import com.cms.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepo extends MongoRepository<Comment,String> {
    List<Comment> findByUserId(String userId);
}
