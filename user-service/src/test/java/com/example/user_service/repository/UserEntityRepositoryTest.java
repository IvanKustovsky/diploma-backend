package com.example.user_service.repository;

import com.example.user_service.audit.AuditConfig;
import com.example.user_service.entity.UserEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(AuditConfig.class)
class UserEntityRepositoryTest {

    @Autowired
    private UserRepository userRepositoryTest;

    @BeforeAll
    static void setUp() {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername("testUser")
                .password("password").roles("USER").build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void findByEmailThatRegistered() {
        // given
        var user = UserEntity.builder().email("usertestemail@gmail.com")
                .password("hashedPassword").fullName("Doe Joe")
                .id(12L).mobileNumber("3809678531").build();
        String email = "usertestemail@gmail.com";
        userRepositoryTest.save(user);

        // when
        Optional<UserEntity> optionalUser = userRepositoryTest.findByEmail(email);

        // then
        assertTrue(optionalUser.isPresent());
    }

    @Test
    void findByEmailThatIsNotRegistered() {
        // given
        String email = "useremail@gmail.com";

        // when
        Optional<UserEntity> optionalUser = userRepositoryTest.findByEmail(email);

        // then
        assertFalse(optionalUser.isPresent());
    }

    @Test
    void findByEmailEqualsNull() {
        // when
        Optional<UserEntity> optionalUser = userRepositoryTest.findByEmail(null);

        // then
        assertFalse(optionalUser.isPresent());
    }
}