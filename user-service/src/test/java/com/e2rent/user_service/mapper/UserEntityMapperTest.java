package com.e2rent.user_service.mapper;

import com.e2rent.user_service.dto.CompanyDto;
import com.e2rent.user_service.dto.RegisterUserDto;
import com.e2rent.user_service.dto.UserDto;
import com.e2rent.user_service.entity.Company;
import com.e2rent.user_service.entity.UserEntity;
import org.junit.jupiter.api.Test;

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

        Company company = new Company();
        company.setCompanyId(1L);
        company.setName("Tech Solutions");
        user.setCompany(company);

        // when
        UserDto userDto = UserMapper.INSTANCE.toDto(user);

        // then
        assertEquals(user.getFullName(), userDto.getFullName());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getMobileNumber(), userDto.getMobileNumber());
        assertEquals(user.getCompany().getName(), userDto.getCompany().getName());
    }

    @Test
    void shouldMapUserDtoToEntity() {
        // given
        RegisterUserDto userDto = RegisterUserDto.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .mobileNumber("+380987654321")
                .password("password321").build();

        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("Tech Solutions");
        userDto.setCompany(companyDto);

        // when
        UserEntity user = UserMapper.INSTANCE.toEntity(userDto);

        // then
        String[] fullName = user.getFullName().split(" ");
        String userFirstName = fullName[0];
        String userLastName = fullName[1];
        assertEquals(userDto.getFirstName(), userFirstName);
        assertEquals(userDto.getLastName(), userLastName);
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getMobileNumber(), user.getMobileNumber());
        assertEquals(userDto.getCompany().getName(), user.getCompany().getName());
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
        assertNull(userDto.getCompany());
    }

    @Test
    void shouldHandleNullFieldsInUserDto() {
        // given
        RegisterUserDto userDto = RegisterUserDto.builder().build();
        // firstName, lastName, email, mobileNumber, password, company are null

        // when
        UserEntity user = UserMapper.INSTANCE.toEntity(userDto);

        // then
        assertNull(userDto.getFirstName());
        assertNull(userDto.getLastName());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getMobileNumber(), user.getMobileNumber());
        assertNull(user.getCompany());
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