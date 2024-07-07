package com.cms.services;

import com.cms.config.CustomUserDetails;
import com.cms.entity.User;
import com.cms.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class CustomerUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private CustomerRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        } else {
            return new CustomUserDetails(user);

        }
    }
}