package com.e2rent.rent_service.repository;

import com.e2rent.rent_service.dto.RentalResponseDto;
import com.e2rent.rent_service.entity.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("SELECT new com.e2rent.rent_service.dto.RentalResponseDto(" +
            "r.id, r.equipmentId, r.renterId, r.ownerId, " +
            "r.startDate, r.endDate, r.address, " +
            "r.status, r.ownerMessage) " +
            "FROM Rental r WHERE r.renterId = :renterId")
    Page<RentalResponseDto> findAllByRenterId(@Param("renterId") Long renterId, Pageable pageable);

    @Query("SELECT new com.e2rent.rent_service.dto.RentalResponseDto(" +
            "r.id, r.equipmentId, r.renterId, r.ownerId, " +
            "r.startDate, r.endDate, r.address, " +
            "r.status, r.ownerMessage) " +
            "FROM Rental r WHERE r.ownerId = :ownerId")
    Page<RentalResponseDto> findAllByOwnerId(@Param("ownerId") Long ownerId, Pageable pageable);
}
