package com.example.user_service.mapper;

import com.example.user_service.dto.CompanyDto;
import com.example.user_service.entity.Company;

import java.util.Objects;

public class CompanyMapper {

    public static CompanyDto mapToCompanyDto(Company company, CompanyDto companyDto) {
        if (Objects.isNull(company) || Objects.isNull(companyDto)) { //TODO: Do something about null
            return null;
        }
        companyDto.setName(company.getName());
        companyDto.setCode(company.getCode());
        companyDto.setAddress(company.getAddress());
        return companyDto;
    }

    public static Company mapToCompany(CompanyDto companyDto, Company company) {
        if (Objects.isNull(companyDto) || Objects.isNull(company)) {
            return null;
        }
        company.setName(companyDto.getName());
        company.setCode(companyDto.getCode());
        company.setAddress(companyDto.getAddress());
        return company;
    }
}
