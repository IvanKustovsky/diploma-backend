package com.example.identity_service.service.impl;

import com.example.identity_service.dto.UserDto;
import com.example.identity_service.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.warn("Searching for user with email: {}", email);
        // TODO: Refactor using OpenFeign
        try {
            var userDto = restTemplate.getForObject(
                    "http://USERS/api/v1?email=" + email, UserDto.class);

            log.warn("Finish search for user with credentials: {}", userDto);
            return new UserPrincipal(userDto);
        } catch (HttpClientErrorException.NotFound e) {
            log.error("User with email {} not found", email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        } catch (Exception e) {
            log.error("Error occurred while searching for user with email {}: {}", email, e.getMessage());
            throw new UsernameNotFoundException("An error occurred while searching for user with email: " + email);
        }
    }
}
