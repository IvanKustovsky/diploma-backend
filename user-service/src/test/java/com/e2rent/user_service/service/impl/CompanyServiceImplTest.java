package com.e2rent.user_service.service.impl;

import com.e2rent.user_service.dto.CompanyDto;
import com.e2rent.user_service.entity.Company;
import com.e2rent.user_service.exception.ResourceNotFoundException;
import com.e2rent.user_service.mapper.CompanyMapper;
import com.e2rent.user_service.repository.CompanyRepository;
import com.e2rent.user_service.exception.CompanyAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Company Service Test Class")
class CompanyServiceImplTest {

    private static final Long COMPANY_ID = 123L;
    private static final String COMPANY_CODE = "44553322";
    private static final String COMPANY_NAME = "Company Name";
    private static final String COMPANY_ADDRESS = "Company address";
    private static final Company TEST_COMPANY = Company.builder()
            .companyId(COMPANY_ID)
            .name(COMPANY_NAME)
            .address(COMPANY_ADDRESS)
            .code(COMPANY_CODE)
            .build();

    @Mock
    private CompanyRepository companyRepositoryMock;

    @InjectMocks
    private CompanyServiceImpl companyServiceImplTest;

    @Test
    @DisplayName("Should throw CompanyAlreadyExistsException when trying to register company with provided code")
    void registerCompanyWhenExists() {
        // given
        Company newCompany = new Company();
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode(COMPANY_CODE);
        newCompany.setCode(COMPANY_CODE);

        when(companyRepositoryMock.findByCode(COMPANY_CODE)).thenReturn(Optional.of(newCompany));

        // when, then
        assertThatThrownBy(() -> companyServiceImplTest.registerCompany(companyDto))
                .isInstanceOf(CompanyAlreadyExistsException.class)
                .hasMessage("Company already registered with given code " + COMPANY_CODE);

        verify(companyRepositoryMock, times(1)).findByCode(COMPANY_CODE);
        verify(companyRepositoryMock, never()).save(any(Company.class));
    }

    @Test
    @DisplayName("Should register company when no company exists for the given code")
    void registerCompanyWhenExistsWhenNotExists() {
        // given
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCode(COMPANY_CODE);
        companyDto.setName(COMPANY_NAME);
        companyDto.setAddress(COMPANY_ADDRESS);

        when(companyRepositoryMock.findByCode(COMPANY_CODE)).thenReturn(Optional.empty());
        when(companyRepositoryMock.save(any(Company.class))).thenReturn(TEST_COMPANY);

        // when
        companyServiceImplTest.registerCompany(companyDto);

        // then
        verify(companyRepositoryMock, times(1)).findByCode(COMPANY_CODE);

        ArgumentCaptor<Company> companyArgumentCaptor = ArgumentCaptor.forClass(Company.class);
        verify(companyRepositoryMock, times(1)).save(companyArgumentCaptor.capture());

        Company savedCompany = companyArgumentCaptor.getValue();

        assertEquals(COMPANY_CODE, savedCompany.getCode());
        assertEquals(COMPANY_NAME, savedCompany.getName());
        assertEquals(COMPANY_ADDRESS, savedCompany.getAddress());
    }

    @Test
    @DisplayName("Should fetch company when it exists for the given code")
    void fetchCompanyWhenExists() {
        // given
        CompanyDto companyDto = CompanyMapper.INSTANCE.toDto(TEST_COMPANY);

        when(companyRepositoryMock.findByCode(COMPANY_CODE)).thenReturn(Optional.of(TEST_COMPANY));

        // when
        CompanyDto result = companyServiceImplTest.fetchCompany(COMPANY_CODE);

        // then
        assertEquals(companyDto, result);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when company does not exist for the given code")
    void fetchCompanyWhenNotExists() {
        // given
        when(companyRepositoryMock.findByCode(COMPANY_CODE)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () ->
                companyServiceImplTest.fetchCompany(COMPANY_CODE)
        );
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when the company code is invalid")
    void fetchCompanyWhenCodeIsInvalid() {
        // given
        String invalidCode = "invalid_code";

        // when & then
        assertThrows(ResourceNotFoundException.class, () ->
                companyServiceImplTest.fetchCompany(invalidCode)
        );
    }

    @Test
    @DisplayName("Should update company when it exists for the given code")
    void updateCompanyWhenExists() {
        // given
        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("New Name");
        companyDto.setAddress("New Address");

        when(companyRepositoryMock.findByCode(COMPANY_CODE)).thenReturn(Optional.of(TEST_COMPANY));

        // when
        boolean result = companyServiceImplTest.updateCompany(COMPANY_CODE, companyDto);

        // then
        assertTrue(result);
        assertEquals(companyDto.getName(), TEST_COMPANY.getName());
        assertEquals(companyDto.getAddress(), TEST_COMPANY.getAddress());
        verify(companyRepositoryMock, times(1)).findByCode(COMPANY_CODE);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when company does not exist for the given code")
    void updateCompanyWhenNotExists() {
        // given
        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("New Name");
        companyDto.setAddress("New Address");

        when(companyRepositoryMock.findByCode(COMPANY_CODE)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> companyServiceImplTest.updateCompany(COMPANY_CODE, companyDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("%s not found with the given input data %s: '%s'",
                        "Company", "code", COMPANY_CODE);

        verify(companyRepositoryMock, times(1)).findByCode(COMPANY_CODE);
        verify(companyRepositoryMock, never()).save(any(Company.class));
    }

    @Test
    @DisplayName("Should delete company when it exists for the given code")
    void deleteCompanyWhenExists() {
        // given
        when(companyRepositoryMock.findByCode(COMPANY_CODE)).thenReturn(Optional.of(TEST_COMPANY));
        doNothing().when(companyRepositoryMock).deleteById(TEST_COMPANY.getCompanyId());

        // when
        boolean result = companyServiceImplTest.deleteCompany(COMPANY_CODE);

        // then
        assertTrue(result);
        verify(companyRepositoryMock, times(1)).findByCode(COMPANY_CODE);
        verify(companyRepositoryMock, times(1)).deleteById(TEST_COMPANY.getCompanyId());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when company does not exist for the given code")
    void deleteCompanyWhenNotExists() {
        // given
        when(companyRepositoryMock.findByCode(COMPANY_CODE)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> companyServiceImplTest.deleteCompany(COMPANY_CODE))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("%s not found with the given input data %s: '%s'",
                        "Company", "code", COMPANY_CODE);

        verify(companyRepositoryMock, times(1)).findByCode(COMPANY_CODE);
        verify(companyRepositoryMock, never()).deleteById(anyLong());
    }
}