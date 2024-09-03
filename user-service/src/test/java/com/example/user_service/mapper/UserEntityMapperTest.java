package com.example.user_service.mapper;

import com.example.user_service.dto.CompanyDto;
import com.example.user_service.dto.UserDto;
import com.example.user_service.entity.Company;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityMapperTest {


    @Test
    void shouldMapUserToDto() {
        // given
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setFullName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setMobileNumber("1234567890");
        user.setPassword("password123");

        Company company = new Company();
        company.setCompanyId(1L);
        company.setName("Tech Solutions");
        user.setCompany(company);

        Role role = new Role(1, "ADMIN");
        user.setRoles(List.of(role));

        // when
        UserDto userDto = UserMapper.INSTANCE.toDto(user);

        // then
        assertEquals(user.getFullName(), userDto.getFullName());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getMobileNumber(), userDto.getMobileNumber());
        assertEquals(user.getPassword(), userDto.getPassword());
        assertEquals(user.getCompany().getName(), userDto.getCompany().getName());
        assertEquals(user.getRoles().size(), userDto.getRoles().size());
        assertEquals(user.getRoles().getFirst().getName(), userDto.getRoles().getFirst().getName());
    }

    @Test
    void shouldMapUserDtoToEntity() {
        // given
        UserDto userDto = UserDto.builder()
                .fullName("Jane Doe")
                .email("jane.doe@example.com")
                .mobileNumber("+380987654321")
                .password("password321").build();

        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("Tech Solutions");
        userDto.setCompany(companyDto);

        Role role = new Role(1, "ADMIN");
        userDto.setRoles(List.of(role));

        // when
        UserEntity user = UserMapper.INSTANCE.toEntity(userDto);

        // then
        assertEquals(userDto.getFullName(), user.getFullName());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getMobileNumber(), user.getMobileNumber());
        assertEquals(userDto.getPassword(), user.getPassword());
        assertEquals(userDto.getCompany().getName(), user.getCompany().getName());
        assertEquals(userDto.getRoles().size(), user.getRoles().size());
        assertEquals(userDto.getRoles().getFirst().getName(), user.getRoles().getFirst().getName());
    }

    @Test
    void shouldHandleNullFieldsInUser() {
        // given
        UserEntity user = new UserEntity();
        user.setId(1L);
        // FullName, email, mobileNumber, password, company, and roles are null

        // when
        UserDto userDto = UserMapper.INSTANCE.toDto(user);

        // then
        assertEquals(user.getFullName(), userDto.getFullName());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getMobileNumber(), userDto.getMobileNumber());
        assertEquals(user.getPassword(), userDto.getPassword());
        assertNull(userDto.getCompany());
        assertTrue(userDto.getRoles().isEmpty());
    }

    @Test
    void shouldHandleNullFieldsInUserDto() {
        // given
        UserDto userDto = UserDto.builder().build();
        // fullName, email, mobileNumber, password, company, and roles are null

        // when
        UserEntity user = UserMapper.INSTANCE.toEntity(userDto);

        // then
        assertEquals(userDto.getFullName(), user.getFullName());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getMobileNumber(), user.getMobileNumber());
        assertEquals(userDto.getPassword(), user.getPassword());
        assertNull(user.getCompany());
        assertNull(user.getRoles());
    }

    @Test
    void shouldHandleUserDtoEqualsNull() {
        // when
        UserEntity user = UserMapper.INSTANCE.toEntity(null);

        // then
        assertNull(user);
    }

    @Test
    void shouldHandleUserEqualsNull() {
        // when
        UserDto userDto = UserMapper.INSTANCE.toDto(null);

        // then
        assertNull(userDto);
    }
}