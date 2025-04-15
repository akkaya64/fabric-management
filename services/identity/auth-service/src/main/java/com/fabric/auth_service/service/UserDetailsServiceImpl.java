package com.fabric.auth_service.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fabric.auth_service.client.UserServiceClient;
import com.fabric.auth_service.payload.response.UserResponse;

import feign.FeignException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserServiceClient userServiceClient;

    public UserDetailsServiceImpl(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserResponse userResponse = userServiceClient.getUserByUsername(username).getBody();

            if (userResponse == null) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }

            List<SimpleGrantedAuthority> authorities = userResponse.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // Note: Spring Security expects a password, but since we're using JWT,
            // we don't need the actual password for authentication.
            // We're using an empty string here but in a real scenario, you might need
            // to retrieve the encoded password from the user service.
            return new User(
                    userResponse.getUsername(),
                    "", // Password is not needed for JWT authentication
                    userResponse.isActive(),
                    true, // accountNonExpired
                    true, // credentialsNonExpired
                    true, // accountNonLocked
                    authorities);

        } catch (FeignException.NotFound e) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching user data", e);
        }
    }
}