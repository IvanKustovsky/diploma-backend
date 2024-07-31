package com.example.user_service.mapper;

import com.example.user_service.dto.CompanyDto;
import com.example.user_service.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    CompanyDto toDto(Company company);

    Company toEntity(CompanyDto companyDto);
}
