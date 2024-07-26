package com.example.user_service.service.impl;

import com.example.user_service.dto.CompanyDto;
import com.example.user_service.entity.Company;
import com.example.user_service.repository.CompanyRepository;
import com.example.user_service.service.ICompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// TODO: Implement this class and add java docs
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements ICompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public void registerCompany(Company company) {

    }

    @Override
    public CompanyDto fetchCompany(String companyCode) {
        return null;
    }

    @Override
    public boolean updateCompany(CompanyDto companyDto) {
        return false;
    }

    @Override
    public boolean deleteCompany(String companyCode) {
        return false;
    }
}
