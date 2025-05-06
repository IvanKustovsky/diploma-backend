package com.e2rent.equipment.repository;

import com.e2rent.equipment.entity.Equipment;
import com.e2rent.equipment.entity.Image;
import com.e2rent.equipment.enums.EquipmentCategory;
import com.e2rent.equipment.enums.EquipmentCondition;
import com.e2rent.equipment.enums.EquipmentStatus;
import com.e2rent.equipment.enums.EquipmentSubcategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class EquipmentRepositoryTest {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Test
    void findEquipmentById() {
        // given
        Equipment equipment = new Equipment();
        equipment.setName("Equipment");
        equipment.setCategory(EquipmentCategory.TOOLS);
        equipment.setSubcategory(EquipmentSubcategory.CHARGING_STATION);
        equipment.setPricePerDay(BigDecimal.valueOf(150.00));
        equipment.setCondition(EquipmentCondition.NEW);
        equipment.setUserId(-14L);
        equipment.setStatus(EquipmentStatus.AVAILABLE);

        equipmentRepository.save(equipment);

        Image mainImage = new Image();
        mainImage.setName("main_image.jpg");
        mainImage.setEquipment(equipment);

        Image additionalImage1 = new Image();
        additionalImage1.setName("image1.jpg");
        additionalImage1.setEquipment(equipment);

        Image additionalImage2 = new Image();
        additionalImage2.setName("image2.jpg");
        additionalImage2.setEquipment(equipment);

        imageRepository.save(mainImage);
        imageRepository.save(additionalImage1);
        imageRepository.save(additionalImage2);

        equipment.setMainImage(mainImage);
        equipment.addImage(additionalImage1);
        equipment.addImage(additionalImage2);

        equipmentRepository.save(equipment);

        // when
        Optional<Equipment> result = equipmentRepository.findEquipmentById(equipment.getEquipmentId());

        // then
        assertTrue(result.isPresent());
        Equipment foundEquipment = result.get();
        assertEquals(equipment.getEquipmentId(), foundEquipment.getEquipmentId());
        assertEquals(2, foundEquipment.getImages().size());
        assertEquals(mainImage.getId(), foundEquipment.getMainImage().getId());
        assertEquals(equipment.getName(), foundEquipment.getName());
        assertEquals(equipment.getCategory(), foundEquipment.getCategory());
        assertEquals(equipment.getPricePerDay(), foundEquipment.getPricePerDay());
        assertEquals(equipment.getCondition(), foundEquipment.getCondition());
    }
}