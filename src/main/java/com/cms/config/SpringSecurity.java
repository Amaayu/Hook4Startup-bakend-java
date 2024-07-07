package com.cms.config;

import com.cms.services.CustomerUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity

public class SpringSecurity {

    private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    public SpringSecurity(org.springframework.security.core.userdetails.UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.security.core.userdetails.UserDetailsService userDetailsService() {
        return new CustomerUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider grtAuthenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;

    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests().
                 requestMatchers("/cms/**")
                .authenticated().requestMatchers("/post/**").
                 authenticated().requestMatchers("/comment/**").
                authenticated().requestMatchers("/like/**").
                 permitAll()
                .anyRequest()
                .permitAll()
                .and()
                .httpBasic(withDefaults());
        http.httpBasic(withDefaults())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy
                                .STATELESS))
                .csrf(AbstractHttpConfigurer::disable);

          return http.build();
    }


}
