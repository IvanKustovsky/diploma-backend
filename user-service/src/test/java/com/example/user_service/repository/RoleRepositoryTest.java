package com.example.user_service.repository;

import com.example.user_service.constants.RoleConstants;
import com.example.user_service.entity.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeAll
    static void init(@Autowired RoleRepository roleRepository) {
        roleRepository.saveAll(List.of(
                Role.builder().name(RoleConstants.USER_ROLE).build(),
                Role.builder().name(RoleConstants.ADMIN_ROLE).build(),
                Role.builder().name(RoleConstants.MANAGER_ROLE).build()));
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