package com.example.user_service.service.impl;

import com.example.user_service.annotation.ValidCompanyCode;
import com.example.user_service.dto.CompanyDto;
import com.example.user_service.entity.Company;
import com.example.user_service.exception.CompanyAlreadyExistsException;
import com.example.user_service.exception.ResourceNotFoundException;
import com.example.user_service.mapper.CompanyMapper;
import com.example.user_service.repository.CompanyRepository;
import com.example.user_service.service.ICompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompanyServiceImpl implements ICompanyService {

    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public Company registerCompany(@Valid CompanyDto companyDto) {
        companyRepository.findByCode(companyDto.getCode())
                .ifPresent(company -> {
                    throw new CompanyAlreadyExistsException("Company already registered with given code "
                            + companyDto.getCode());
                });
        return companyRepository.save(CompanyMapper.INSTANCE.toEntity(companyDto));
    }

    @Override
    public CompanyDto fetchCompany(@ValidCompanyCode String companyCode) {
        return companyRepository.findByCode(companyCode)
                .map(CompanyMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "code", companyCode));
    }

    @Override
    @Transactional
    public boolean updateCompany(@ValidCompanyCode String code, @Valid CompanyDto companyDto) {
        Company existingCompany = companyRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "code", code));

        existingCompany.setName(companyDto.getName());
        existingCompany.setAddress(companyDto.getAddress());
        return true;
    }

    @Override
    @Transactional
    public boolean deleteCompany(@ValidCompanyCode String companyCode) {
        var optionalCompany = companyRepository.findByCode(companyCode)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "code", companyCode));
        companyRepository.deleteById(optionalCompany.getCompanyId());
        return true;
    }
}
