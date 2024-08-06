package com.example.user_service.mapper;

import com.example.user_service.dto.CompanyDto;
import com.example.user_service.entity.Company;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompanyMapperTest {

    @Test
    void shouldMapCompanyToDto() {
        // given
        Company company = new Company();
        company.setCompanyId(1L);
        company.setName("Tech Solutions");
        company.setCode("TS123");
        company.setAddress("123 Main St, Los Angeles");

        // when
        CompanyDto companyDto = CompanyMapper.INSTANCE.toDto(company);

        // then
        assertEquals(company.getName(), companyDto.getName());
        assertEquals(company.getCode(), companyDto.getCode());
        assertEquals(company.getAddress(), companyDto.getAddress());
    }

    @Test
    void shouldMapCompanyDtoToEntity() {
        // given
        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("Tech Solutions");
        companyDto.setCode("TS123");
        companyDto.setAddress("123 Main St, Washington DC");

        // when
        Company company = CompanyMapper.INSTANCE.toEntity(companyDto);

        // then
        assertEquals(companyDto.getName(), company.getName());
        assertEquals(companyDto.getCode(), company.getCode());
        assertEquals(companyDto.getAddress(), company.getAddress());
    }

    @Test
    void shouldHandleNullFieldsInCompany() {
        // given
        Company company = new Company();
        company.setCompanyId(1L);
        // name, code, and address are null

        // when
        CompanyDto companyDto = CompanyMapper.INSTANCE.toDto(company);

        // then
        assertEquals(company.getName(), companyDto.getName());
        assertEquals(company.getCode(), companyDto.getCode());
        assertEquals(company.getAddress(), companyDto.getAddress());
    }

    @Test
    void shouldHandleNullFieldsInCompanyDto() {
        // given
        CompanyDto companyDto = new CompanyDto();
        // name, code, and address are null

        // when
        Company company = CompanyMapper.INSTANCE.toEntity(companyDto);

        // then
        assertEquals(companyDto.getName(), company.getName());
        assertEquals(companyDto.getCode(), company.getCode());
        assertEquals(companyDto.getAddress(), company.getAddress());
    }

    @Test
    void shouldHandleCompanyDtoEqualsNull() {
        // when
        Company company = CompanyMapper.INSTANCE.toEntity(null);

        // then
        assertNull(company);
    }

    @Test
    void shouldHandleCompanyEqualsNull() {
        // when
        CompanyDto companyDto = CompanyMapper.INSTANCE.toDto(null);

        // then
        assertNull(companyDto);
    }
}