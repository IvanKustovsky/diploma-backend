package com.example.user_service.service;

import com.example.user_service.annotation.ValidCompanyCode;
import com.example.user_service.dto.CompanyDto;
import com.example.user_service.entity.Company;
import jakarta.validation.Valid;

public interface ICompanyService {

    Company registerCompany(@Valid CompanyDto companyDto);

    CompanyDto fetchCompany(@ValidCompanyCode String companyCode);

    boolean updateCompany(@Valid CompanyDto companyDto);

    boolean deleteCompany(@ValidCompanyCode String companyCode);

    // TODO: Add java docs and perhaps change some method signatures
}
