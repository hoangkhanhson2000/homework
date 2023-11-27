package com.example.demo.service;

import com.example.demo.entity.Users;
import com.example.demo.entity.Roles;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        Users users = userOptional.get();

        String roles = getRoles(users);

        return org.springframework.security.core.userdetails.User.builder()
                .username(users.getUsername())
                .password(users.getPassword())
                .roles(roles)
                .build();
    }

    private String getRoles(Users users) {
        Set<Roles> roles = users.getRoles();
        return roles.stream()
                .map(Roles::getRoleName)
                .collect(Collectors.joining(","));
    }
}
