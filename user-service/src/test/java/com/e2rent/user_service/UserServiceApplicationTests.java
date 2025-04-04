package com.e2rent.user_service;

import com.e2rent.user_service.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoBean(types = {JwtDecoder.class, SecurityConfig.class})
class UserServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
