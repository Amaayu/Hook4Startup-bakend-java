package com.cms.controller;

import com.cms.Dto.PostDto;
import com.cms.entity.Post;
import com.cms.entity.User;
import com.cms.entity.UserProfile;
import com.cms.repository.CustomerRepo;
import com.cms.repository.PostRepo;
import com.cms.repository.UserProfileRepo;
import com.cms.services.PostServices;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/post")
public class PostController {
    @Autowired
    UserProfileRepo userProfileRepo;

    @Autowired
    PostServices postServices;

    @Autowired
    Post newPost;

    @Autowired
    CustomerRepo customerRepo;


    @Autowired
    PostRepo postRepo;

    // create post by user
    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody PostDto postDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }

            String nameofUser = authentication.getName();
            Optional<User> userOptional = Optional.ofNullable(customerRepo.findByUsername(nameofUser));
            if (!userOptional.isPresent()) {
                return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
            }
            User user = userOptional.get();

           if (!user.isMakeProfileStatus()){
               return new ResponseEntity<>("UserProfile not found plz make", HttpStatus.BAD_REQUEST);
           }
            newPost.setPostId(new ObjectId().toString());
            newPost.setUserId(user);
            newPost.setContent(postDto.getContent());
            postServices.PostSave(newPost);
            user.getPosts().add(newPost);  // Add the new post to the user's list of posts
            customerRepo.save(user);
            UserProfile userProfileByUserId = userProfileRepo.findUserProfileByUserId(user.getId());
            userProfileByUserId.setNumberOfPosts(postRepo.findByUserId(user.getId()).size());
            userProfileRepo.save(userProfileByUserId);

            return new ResponseEntity<>("Created Done", HttpStatus.CREATED);
        } catch (Exception e) {

            e.printStackTrace();
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // update post by user
      @PutMapping("/update")
      public ResponseEntity<?> PostUpdate(@RequestBody PostDto postDto) {

          try {
              Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
              if (authentication == null || !authentication.isAuthenticated()) {
                  return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
              }

              String nameofUser = authentication.getName();
              Optional<User> userOptional = Optional.ofNullable(customerRepo.findByUsername(nameofUser));
              if (!userOptional.isPresent()) {
                  return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
              }
              User user = userOptional.get();

              if (!user.isMakeProfileStatus()){
                  return new ResponseEntity<>("UserProfile not found plz make", HttpStatus.BAD_REQUEST);
              }

              Optional<Post> byId = postRepo.findById(postDto.getPostId());
              if (!byId.isPresent()) {
                  return new ResponseEntity<>("Post not found", HttpStatus.BAD_REQUEST);
              }
              Post post = byId.get();
              post.setContent(postDto.getContent());
              postServices.postUpdate(post);
              return new ResponseEntity<>("Update successfully", HttpStatus.OK);


          } catch (Exception e) {
              throw new RuntimeException(e);
          }
      }
// get all post
    @GetMapping("/all")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        try {
            List<Post> postList = postRepo.findAll();
            List<PostDto> postDTOList = postList.stream().map(post -> {
                return new PostDto(

                        post.getUserId().getUsername(),
                        post.getPostId(),
                        post.getContent(),
                        post.getComments().size(),
                        post.getLikes().size(),
                        post.getUserId().getPosts().size(),
                        post.getUserId().isMakeProfileStatus()

                );
            }).collect(Collectors.toList());
            return new ResponseEntity<>(postDTOList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}


