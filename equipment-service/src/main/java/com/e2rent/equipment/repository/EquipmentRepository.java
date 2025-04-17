package com.e2rent.equipment.repository;

import com.e2rent.equipment.dto.EquipmentSummaryDto;
import com.e2rent.equipment.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    @Query("SELECT new com.e2rent.equipment.dto.EquipmentSummaryDto(e.equipmentId, e.name, e.mainImage.id)" +
            " FROM Equipment e WHERE e.mainImage IS NOT NULL")
    Page<EquipmentSummaryDto> findAllWithMainImage(Pageable pageable);

    @Query("SELECT e FROM Equipment e LEFT JOIN FETCH e.mainImage " +
            "LEFT JOIN FETCH e.images WHERE e.equipmentId = :equipmentId")
    Optional<Equipment> findEquipmentById(@Param("equipmentId") Long equipmentId);

    @Query("SELECT new com.e2rent.equipment.dto.EquipmentSummaryDto(e.equipmentId, e.name, " +
            "CASE WHEN e.mainImage IS NOT NULL THEN e.mainImage.id ELSE NULL END) " +
            "FROM Equipment e WHERE e.userId = :userId")
    Page<EquipmentSummaryDto> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}
