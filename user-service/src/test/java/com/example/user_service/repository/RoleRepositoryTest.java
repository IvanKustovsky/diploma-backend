package com.example.user_service.repository;

import com.example.user_service.audit.AuditConfig;
import com.example.user_service.constants.RoleConstants;
import com.example.user_service.entity.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@DataJpaTest
@Import(AuditConfig.class)
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeAll
    static void setUp() {
        UserDetails userDetails = User.withUsername("testUser")
                .password("password").roles("USER").build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getRoleByNameContainsUser() {
        // when
        Optional<Role> optionalRole = roleRepository.getRoleByName(RoleConstants.USER_ROLE);

        // then
        assertTrue(optionalRole.isPresent());
    }

    @Test
    void getRoleByNameContainsAdmin() {
        // when
        Optional<Role> optionalRole = roleRepository.getRoleByName(RoleConstants.ADMIN_ROLE);

        // then
        assertTrue(optionalRole.isPresent());
    }

    @Test
    void getRoleByNameContainsManager() {

        // when
        Optional<Role> optionalRole = roleRepository.getRoleByName(RoleConstants.MANAGER_ROLE);

        // then
        assertTrue(optionalRole.isPresent());
    }

    @Test
    void getRoleByNameDoesNotContainOtherRole() {
        // given
        String roleName = "DEVELOPER";

        // when
        Optional<Role> optionalRole = roleRepository.getRoleByName(roleName);

        // then
        assertFalse(optionalRole.isPresent());
    }

    @Test
    void getRoleByNameEqualsNull() {
        // when
        Optional<Role> optionalRole = roleRepository.getRoleByName(null);

        // then
        assertFalse(optionalRole.isPresent());
    }
}