package com.cms.controller;

import com.cms.Dto.LoginDto;
import com.cms.Dto.UserDto;
import com.cms.entity.User;
import com.cms.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RequestMapping("/")
@RestController
public class PublicUserController {
    @Autowired
    public User user;

    @Autowired
    public PasswordEncoder passwordEncoder;
    @Autowired
    private UserServices userServices;

    // Login Routes or EndPoints
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        // Logic to authenticate user
        try {
            User user = userServices.findCustomerByUserName(loginDto.getUsername());
            if (user != null && passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                return new ResponseEntity<>("Login successfully", HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // sing up Routes or create new user
    @PostMapping("/create")
    public ResponseEntity<?> createEntry(@RequestBody UserDto userDto) {
        try {
            user.setUsername(userDto.getUsername());
            user.setPassword(userDto.getPassword());
            userServices.customerSave( user);
            return new ResponseEntity<>("Created Done" , HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
