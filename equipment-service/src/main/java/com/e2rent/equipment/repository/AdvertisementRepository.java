package com.e2rent.equipment.repository;

import com.e2rent.equipment.dto.AdvertisementDto;
import com.e2rent.equipment.entity.Advertisement;
import com.e2rent.equipment.enums.AdvertisementStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    Optional<Advertisement> findByEquipment_EquipmentId(Long equipmentId);

    @Query("SELECT new com.e2rent.equipment.dto.AdvertisementDto(" +
            "a.id, e.equipmentId, e.name, e.price, e.mainImage.id, a.status, a.adminMessage) " +
            "FROM Advertisement a " +
            "JOIN a.equipment e " +
            "WHERE a.status = :status AND e.status = 'AVAILABLE'")
    Page<AdvertisementDto> findAllByStatus(@Param("status") AdvertisementStatus status, Pageable pageable);

    @Query("SELECT new com.e2rent.equipment.dto.AdvertisementDto(" +
            "a.id, e.equipmentId, e.name, e.price, e.mainImage.id, a.status, a.adminMessage) " +
            "FROM Advertisement a " +
            "JOIN a.equipment e " +
            "WHERE a.status IN :statuses AND e.status = 'AVAILABLE'")
    Page<AdvertisementDto> findAllByStatusIn(@Param("statuses") List<AdvertisementStatus> statuses, Pageable pageable);

    @Query("""
                SELECT new com.e2rent.equipment.dto.AdvertisementDto(a.id, e.equipmentId, e.name, e.price,
                e.mainImage.id, a.status, a.adminMessage)
                FROM Advertisement a
                JOIN a.equipment e
                WHERE e.userId = :userId AND a.status = :status AND e.status = 'AVAILABLE'
            """)
    Page<AdvertisementDto> findAllApprovedByUserId(@Param("userId") Long userId,
                                                   @Param("status") AdvertisementStatus status, Pageable pageable
    );
}
