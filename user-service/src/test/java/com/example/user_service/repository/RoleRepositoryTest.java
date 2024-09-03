package com.example.user_service.repository;

import com.example.user_service.constants.RoleConstants;
import com.example.user_service.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

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