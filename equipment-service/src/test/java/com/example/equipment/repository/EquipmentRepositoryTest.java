package com.example.equipment.repository;

import com.example.equipment.dto.EquipmentSummaryDto;
import com.example.equipment.entity.Equipment;
import com.example.equipment.entity.Image;
import com.example.equipment.enums.EquipmentCondition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    void findAllWithMainImage() {
        // given
        Equipment equipmentWithoutMainImage = new Equipment();
        equipmentWithoutMainImage.setName("Equipment 1");
        equipmentWithoutMainImage.setCategory("Tools");
        equipmentWithoutMainImage.setPrice(BigDecimal.valueOf(100.00));
        equipmentWithoutMainImage.setCondition(EquipmentCondition.NEW);
        equipmentWithoutMainImage.setUserId(-12L);

        Equipment equipment1 = new Equipment();
        equipment1.setName("Equipment 1");
        equipment1.setCategory("Tools");
        equipment1.setPrice(BigDecimal.valueOf(100.00));
        equipment1.setCondition(EquipmentCondition.NEW);
        equipment1.setUserId(-12L);

        Equipment equipment2 = new Equipment();
        equipment2.setName("Equipment 2");
        equipment2.setCategory("Tools");
        equipment2.setPrice(BigDecimal.valueOf(200.00));
        equipment2.setCondition(EquipmentCondition.USED);
        equipment2.setUserId(-13L);

        equipmentRepository.save(equipmentWithoutMainImage);
        equipmentRepository.save(equipment1);
        equipmentRepository.save(equipment2);

        Image mainImage1 = new Image();
        mainImage1.setName("image1.jpg");
        mainImage1.setEquipment(equipment1);
        imageRepository.save(mainImage1);

        Image mainImage2 = new Image();
        mainImage2.setName("image2.jpg");
        mainImage2.setEquipment(equipment2);
        imageRepository.save(mainImage2);

        equipment1.setMainImage(mainImage1);
        equipment2.setMainImage(mainImage2);
        equipmentRepository.save(equipment1);
        equipmentRepository.save(equipment2);

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<EquipmentSummaryDto> result = equipmentRepository.findAllWithMainImage(pageable);

        // then
        assertFalse(result.isEmpty());
        assertEquals(2, result.getTotalElements());
        assertEquals(equipment1.getEquipmentId(), result.getContent().get(0).getId());
        assertEquals(mainImage1.getId(), result.getContent().get(0).getMainImageId());
        assertEquals(equipment2.getEquipmentId(), result.getContent().get(1).getId());
        assertEquals(mainImage2.getId(), result.getContent().get(1).getMainImageId());
    }

    @Test
    void findEquipmentById() {
        // given
        Equipment equipment = new Equipment();
        equipment.setName("Equipment");
        equipment.setCategory("Tools");
        equipment.setPrice(BigDecimal.valueOf(150.00));
        equipment.setCondition(EquipmentCondition.NEW);
        equipment.setUserId(-14L);

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
        assertEquals(equipment.getPrice(), foundEquipment.getPrice());
        assertEquals(equipment.getCondition(), foundEquipment.getCondition());
    }
}