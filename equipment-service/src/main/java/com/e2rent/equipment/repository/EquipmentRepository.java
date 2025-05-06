package com.e2rent.equipment.repository;

import com.e2rent.equipment.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    @Query("SELECT e FROM Equipment e LEFT JOIN FETCH e.mainImage " +
            "LEFT JOIN FETCH e.images WHERE e.equipmentId = :equipmentId")
    Optional<Equipment> findEquipmentById(@Param("equipmentId") Long equipmentId);

    @Query("SELECT e.userId FROM Equipment e WHERE e.equipmentId = :equipmentId")
    Optional<Long> findOwnerIdByEquipmentId(@Param("equipmentId") Long equipmentId);
}
