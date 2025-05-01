package com.e2rent.rent_service.mapper;

import com.e2rent.rent_service.dto.CreateRentalRequestDto;
import com.e2rent.rent_service.dto.RentalResponseDto;
import com.e2rent.rent_service.entity.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RentalMapper {

    RentalMapper INSTANCE = Mappers.getMapper(RentalMapper.class);

    Rental toRental(CreateRentalRequestDto createRentalRequestDto);

    RentalResponseDto toDto(Rental rental);
}
