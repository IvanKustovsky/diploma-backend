package com.e2rent.user_service.service;

import com.e2rent.user_service.annotation.ValidCompanyCode;
import com.e2rent.user_service.entity.Company;
import com.e2rent.user_service.dto.CompanyDto;
import jakarta.validation.Valid;

public interface ICompanyService {

    /**
     *
     * @param companyDto - CompanyDto Object
     * @return Company Object with provided in companyDto fields
     */
    Company registerCompany(@Valid CompanyDto companyDto);

    /**
     *
     * @param companyCode - Input Company Code
     * @return Company details based on a given company code
     */
    CompanyDto fetchCompany(@ValidCompanyCode String companyCode);

    /**
     *
     * @param code - Input Company Code
     * @param companyDto - CompanyDto Object
     * @return boolean indicating if the update of Company is successful or not
     */
    boolean updateCompany(@ValidCompanyCode String code, @Valid CompanyDto companyDto);

    /**
     *
     * @param companyCode - Input Company Code
     * @return boolean indicating if the delete of Company is successful or not
     */
    boolean deleteCompany(@ValidCompanyCode String companyCode);

}
