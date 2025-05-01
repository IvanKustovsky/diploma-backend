package com.e2rent.rent_service.repository;

import com.e2rent.rent_service.dto.RentalResponseDto;
import com.e2rent.rent_service.entity.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("SELECT new com.e2rent.rent_service.dto.RentalResponseDto(" +
            "r.id, r.equipmentId, r.renterId, r.ownerId, " +
            "r.startDate, r.endDate, r.address, " +
            "r.status, r.ownerMessage) " +
            "FROM Rental r WHERE r.renterId = :renterId")
    Page<RentalResponseDto> findAllByRenterId(@Param("renterId") Long renterId, Pageable pageable);

    @Query("SELECT r FROM Rental r WHERE r.renterId = :renterId")
    List<Rental> findAllByRenterId(@Param("renterId") Long renterId);

    @Query("SELECT new com.e2rent.rent_service.dto.RentalResponseDto(" +
            "r.id, r.equipmentId, r.renterId, r.ownerId, " +
            "r.startDate, r.endDate, r.address, " +
            "r.status, r.ownerMessage) " +
            "FROM Rental r " +
            "WHERE r.ownerId = :ownerId " +
            "ORDER BY CASE WHEN r.status = 'PENDING' THEN 0 ELSE 1 END, r.startDate DESC")
    Page<RentalResponseDto> findAllByOwnerId(@Param("ownerId") Long ownerId, Pageable pageable);

    @Query("SELECT r FROM Rental r WHERE r.ownerId = :ownerId")
    List<Rental> findAllByOwnerId(@Param("ownerId") Long ownerId);
}
