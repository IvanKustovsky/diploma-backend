package com.e2rent.user_service.repository;

import com.e2rent.user_service.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserEntityRepositoryTest {

    @Autowired
    private UserRepository userRepositoryTest;

    @Test
    void findByEmailThatRegistered() {
        // given
        String email = "usertestemail@gmail.com";
        var user = UserEntity.builder().email(email).fullName("Doe Joe").mobileNumber("+380966989321")
                .build();

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