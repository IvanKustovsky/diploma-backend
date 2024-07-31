package com.example.user_service.service;

import com.example.user_service.dto.CompanyDto;
import com.example.user_service.entity.Company;

public interface ICompanyService {

    Company registerCompany(CompanyDto companyDto);

    CompanyDto fetchCompany(String companyCode);

    boolean updateCompany(CompanyDto companyDto);

    boolean deleteCompany(String companyCode);

    // TODO: Add java docs and perhaps change some method signatures
}
