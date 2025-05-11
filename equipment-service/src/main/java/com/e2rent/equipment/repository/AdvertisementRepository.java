package com.e2rent.equipment.repository;

import com.e2rent.equipment.dto.AdvertisementDto;
import com.e2rent.equipment.entity.Advertisement;
import com.e2rent.equipment.enums.AdvertisementStatus;
import com.e2rent.equipment.enums.EquipmentCategory;
import com.e2rent.equipment.enums.EquipmentCondition;
import com.e2rent.equipment.enums.EquipmentSubcategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    Optional<Advertisement> findByEquipment_EquipmentId(Long equipmentId);

    @Query("SELECT new com.e2rent.equipment.dto.AdvertisementDto(" +
            "a.id, e.equipmentId, e.name, e.status, e.pricePerDay, e.mainImage.id, a.status, a.adminMessage) " +
            "FROM Advertisement a " +
            "JOIN a.equipment e " +
            "WHERE a.status IN :statuses AND e.status = 'AVAILABLE'")
    Page<AdvertisementDto> findAllByStatusIn(@Param("statuses") List<AdvertisementStatus> statuses, Pageable pageable);

    @Query("""
                SELECT new com.e2rent.equipment.dto.AdvertisementDto(a.id, e.equipmentId, e.name, e.status,
                 e.pricePerDay, e.mainImage.id, a.status, a.adminMessage)
                FROM Advertisement a
                JOIN a.equipment e
                WHERE e.userId = :userId AND a.status = :status AND e.status = 'AVAILABLE'
            """)
    Page<AdvertisementDto> findAllApprovedByUserId(@Param("userId") Long userId,
                                                   @Param("status") AdvertisementStatus status, Pageable pageable
    );

    @Query("SELECT new com.e2rent.equipment.dto.AdvertisementDto(" +
            "a.id, e.equipmentId, e.name, e.status, e.pricePerDay, e.mainImage.id, a.status, a.adminMessage) " +
            "FROM Advertisement a " +
            "JOIN a.equipment e " +
            "WHERE e.userId = :userId")
    Page<AdvertisementDto> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT new com.e2rent.equipment.dto.AdvertisementDto(" +
            "a.id, e.equipmentId, e.name, e.status, e.pricePerDay, e.mainImage.id, a.status, a.adminMessage) " +
            "FROM Advertisement a " +
            "JOIN a.equipment e " +
            "WHERE a.status = 'APPROVED' AND e.status = 'AVAILABLE' " +
            "AND (:category IS NULL OR e.category = :category) " +
            "AND (:subcategory IS NULL OR e.subcategory = :subcategory) " +
            "AND (:condition IS NULL OR e.condition = :condition) " +
            "AND (:minPrice IS NULL OR e.pricePerDay >= :minPrice) " +
            "AND (:maxPrice IS NULL OR e.pricePerDay <= :maxPrice) " +
            "AND (:keyword IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "     OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:excludedUserId IS NULL OR e.userId <> :excludedUserId)")
    Page<AdvertisementDto> findFilteredAdvertisements(
            @Param("category") EquipmentCategory category,
            @Param("subcategory") EquipmentSubcategory subcategory,
            @Param("condition") EquipmentCondition condition,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("keyword") String keyword,
            @Param("excludedUserId") Long excludedUserId,
            Pageable pageable);
}
