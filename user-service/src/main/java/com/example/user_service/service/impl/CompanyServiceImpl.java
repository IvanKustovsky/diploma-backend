package com.example.user_service.service.impl;

import com.example.user_service.annotation.ValidCompanyCode;
import com.example.user_service.dto.CompanyDto;
import com.example.user_service.entity.Company;
import com.example.user_service.exception.CompanyAlreadyExistsException;
import com.example.user_service.mapper.CompanyMapper;
import com.example.user_service.repository.CompanyRepository;
import com.example.user_service.service.ICompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

// TODO: Implement this class and add java docs
@Service
@Validated
@RequiredArgsConstructor
public class CompanyServiceImpl implements ICompanyService {

    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public Company registerCompany(@Valid CompanyDto companyDto) {
        Optional<Company> company = companyRepository.findByCode(companyDto.getCode());
        if (company.isPresent()) {
            throw new CompanyAlreadyExistsException("Company already registered with given code " + companyDto.getCode());
        }
        return companyRepository.save(CompanyMapper.INSTANCE.toEntity(companyDto));
    }

    @Override
    public CompanyDto fetchCompany(@ValidCompanyCode String companyCode) {
        return null;
    }

    @Override
    public boolean updateCompany(@Valid CompanyDto companyDto) {
        return false;
    }

    @Override
    public boolean deleteCompany(@ValidCompanyCode String companyCode) {
        return false;
    }
}
