package com.cms.controller;

import com.cms.Dto.UserDto;
import com.cms.entity.Post;
import com.cms.entity.User;
import com.cms.entity.UserProfile;
import com.cms.repository.CustomerRepo;
import com.cms.repository.PostRepo;
import com.cms.repository.UserProfileRepo;
import com.cms.services.PostServices;
import com.cms.services.UserProfileServices;
import com.cms.services.UserServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@RestController
@CrossOrigin("*")
@RequestMapping("/cms")
public class UserController {

    @Autowired
    private UserServices userServices;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private UserProfileRepo userProfileRepo;
    @Autowired
    private UserProfile createUserProfile;
    @Autowired
    PostRepo postRepo;
    @Autowired
    UserProfileServices userProfileServices;
    @Autowired
    PostServices postServices;


    //user forget password and username
    @PutMapping("/user/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String username = authentication.getName();

        User user1 = userServices.findCustomerByUserName(username);
        if (user1 == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            user1.setUsername(userDto.getUsername());
            user1.setPassword(userDto.getPassword());
            userServices.customerSave(user1);
            UserProfile userProfileByUserId = userProfileRepo.findUserProfileByUserId(user1.getId());
            userProfileByUserId.setUsername(userDto.getUsername());
            userProfileRepo.save(userProfileByUserId);
            return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }

    // get all users
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        try {
            List<User> users = userServices.findAllCustomer();
            List<UserDto> userDtoList = users.stream().map(user -> {
                return new UserDto(
                        user.getId(),
                        user.getUsername(),
                        user.getPassword()

                );
            }).collect(Collectors.toList());

            return new ResponseEntity<>(userDtoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }




    // Create user profile
    @PostMapping("/profile/create")
    public ResponseEntity<?> createUserProfile(@RequestBody UserProfile userProfile) {

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



            if (user.isMakeProfileStatus()) {
                return new ResponseEntity<>("User has a already ", HttpStatus.BAD_REQUEST);
            }
            // Ensure userProfile has a unique identifier
            userProfile.setId(new ObjectId().toString());

            // Set user properties
            user.setUsername(userProfile.getUsername());
            user.setMakeProfileStatus(true);
            customerRepo.save(user); // Save the user first

            // Set userProfile properties
            userProfile.setUserId(user);
            userProfile.setFullName(userProfile.getFullName());
            userProfile.setBio(userProfile.getBio());
            userProfile.setProfilePictureUrl(userProfile.getProfilePictureUrl());
            userProfile.setNumberOfPosts(postRepo.findByUserId(user.getId()).size());
            userProfile.setNumberOfFollowers("45M");
            userProfile.setNumberOfFollowing("20");

            // Save the userProfile
            userProfileServices.ProfileSave(userProfile);

            // Update user with userProfile id and save again
            user.setUserProfileId(userProfile);
            customerRepo.save(user);
            return new ResponseEntity<>("User profile created", HttpStatus.OK);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // user profile Update
    @PutMapping("/profile/update")
    public ResponseEntity<?> updateUserProfile(@RequestBody UserProfile userProfile) {

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

            UserProfile userProfileByUserId = userProfileRepo.findUserProfileByUserId(user.getId());

            userProfileByUserId.setUsername(userProfile.getUsername());
            user.setUsername(userProfile.getUsername());
            customerRepo.save(user);
            userProfileByUserId.setFullName(userProfile.getFullName());
            userProfileByUserId.setBio(userProfile.getBio());
            userProfileByUserId.setProfilePictureUrl(userProfile.getProfilePictureUrl());

            userProfileServices.userProfileUpdate(userProfileByUserId);
            return new ResponseEntity<>("User profile Updated", HttpStatus.OK);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}




