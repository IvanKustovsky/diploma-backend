package com.e2rent.identity_service.service.impl;

import com.e2rent.identity_service.model.UserPrincipal;
import com.e2rent.identity_service.service.client.UsersFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersFeignClient feignClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Searching for user with email: {}", email);
        var userDto = feignClient.fetchUser(email).getBody();
        log.info("Finish search for user with credentials: {}", userDto);
        return new UserPrincipal(userDto);
    }
}
