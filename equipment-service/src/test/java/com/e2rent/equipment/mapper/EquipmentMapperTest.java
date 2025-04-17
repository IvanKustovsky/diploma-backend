package com.e2rent.equipment.mapper;

import com.e2rent.equipment.dto.EquipmentDto;
import com.e2rent.equipment.entity.Equipment;
import com.e2rent.equipment.entity.Image;
import com.e2rent.equipment.enums.EquipmentCategory;
import com.e2rent.equipment.enums.EquipmentCondition;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EquipmentMapperTest {

    @Test
    void shouldMapEquipmentToDto() {
        // given
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(1L);
        equipment.setName("Diesel Generator");
        equipment.setDescription("Produces electricity by burning diesel fuel");
        equipment.setCategory(EquipmentCategory.DISTRIBUTION);
        equipment.setPrice(BigDecimal.valueOf(150.00));
        equipment.setCondition(EquipmentCondition.NEW);
        equipment.setUserId(1L);

        Image mainImage = new Image();
        mainImage.setId(1L);  // ID основного зображення
        mainImage.setName("main_image.jpg");
        equipment.setMainImage(mainImage);

        Image image1 = new Image();
        image1.setId(2L);  // ID додаткового зображення
        image1.setName("image1.jpg");

        Image image2 = new Image();
        image2.setId(3L);  // ID додаткового зображення
        image2.setName("image2.jpg");

        equipment.setImages(List.of(image1, image2));

        // when
        EquipmentDto equipmentDto = EquipmentMapper.INSTANCE.toEquipmentDto(equipment);

        // then
        assertEquals(equipment.getName(), equipmentDto.getName());
        assertEquals(equipment.getDescription(), equipmentDto.getDescription());
        assertEquals(equipment.getCategory(), equipmentDto.getCategory());
        assertEquals(equipment.getPrice(), equipmentDto.getPrice());
        assertEquals(equipment.getCondition(), equipmentDto.getCondition());

        // Перевіряємо ID основного зображення
        assertEquals(equipment.getMainImage().getId(), equipmentDto.getMainImageId());

        // Перевіряємо ID додаткових зображень
        assertEquals(2, equipmentDto.getImageIds().size());
        assertEquals(Long.valueOf(2), equipmentDto.getImageIds().get(0));  // Перевірка ID для першого зображення
        assertEquals(Long.valueOf(3), equipmentDto.getImageIds().get(1));  // Перевірка ID для другого зображення
    }

    @Test
    void shouldMapEquipmentDtoToEquipment() {
        // given
        EquipmentDto equipmentDto = new EquipmentDto();
        equipmentDto.setName("Diesel Generator");
        equipmentDto.setDescription("Produces electricity by burning diesel fuel");
        equipmentDto.setCategory(EquipmentCategory.BACKUP_POWER);
        equipmentDto.setPrice(BigDecimal.valueOf(150.00));
        equipmentDto.setCondition(EquipmentCondition.NEW);
        equipmentDto.setUserId(1L);

        // when
        Equipment equipment = EquipmentMapper.INSTANCE.toEquipment(equipmentDto);

        // then
        assertEquals(equipmentDto.getName(), equipment.getName());
        assertEquals(equipmentDto.getDescription(), equipment.getDescription());
        assertEquals(equipmentDto.getCategory(), equipment.getCategory());
        assertEquals(equipmentDto.getPrice(), equipment.getPrice());
        assertEquals(equipmentDto.getCondition(), equipment.getCondition());
    }

    @Test
    void shouldHandleNullFieldsInEquipment() {
        // given
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(1L);
        // name, description, category, price, condition, mainImage, and images are null

        // when
        EquipmentDto equipmentDto = EquipmentMapper.INSTANCE.toEquipmentDto(equipment);

        // then
        assertNull(equipmentDto.getName());
        assertNull(equipmentDto.getDescription());
        assertNull(equipmentDto.getCategory());
        assertNull(equipmentDto.getPrice());
        assertNull(equipmentDto.getCondition());
        assertNull(equipmentDto.getMainImageId());
        assertTrue(equipmentDto.getImageIds().isEmpty());
    }

    @Test
    void shouldHandleNullFieldsInEquipmentDto() {
        // given
        EquipmentDto equipmentDto = new EquipmentDto();
        // name, description, category, price, condition, mainImageUrl, and imageUrls are null

        // when
        Equipment equipment = EquipmentMapper.INSTANCE.toEquipment(equipmentDto);

        // then
        assertNull(equipment.getName());
        assertNull(equipment.getDescription());
        assertNull(equipment.getCategory());
        assertNull(equipment.getPrice());
        assertNull(equipment.getCondition());
        assertNull(equipment.getMainImage());
        assertNull(equipment.getImages());
    }

    @Test
    void shouldHandleEquipmentDtoEqualsNull() {
        // when
        Equipment equipment = EquipmentMapper.INSTANCE.toEquipment(null);

        // then
        assertNull(equipment);
    }

    @Test
    void shouldHandleEquipmentEqualsNull() {
        // when
        EquipmentDto equipmentDto = EquipmentMapper.INSTANCE.toEquipmentDto(null);

        // then
        assertNull(equipmentDto);
    }

    @Test
    void shouldUpdateEquipmentFromDto() {
        // given
        Equipment equipment = new Equipment();
        equipment.setName("Old Generator");
        equipment.setCategory(EquipmentCategory.CABLING);
        equipment.setPrice(new BigDecimal(1500));
        equipment.setDescription("Old generator description");
        equipment.setCondition(EquipmentCondition.NEW);

        EquipmentDto equipmentDto = new EquipmentDto();
        equipmentDto.setName("New Generator");
        equipmentDto.setCategory(EquipmentCategory.BACKUP_POWER);
        equipmentDto.setPrice(new BigDecimal(1250));
        equipmentDto.setDescription("New generator description");
        equipmentDto.setCondition(EquipmentCondition.USED);

        // when
        EquipmentMapper.INSTANCE.updateEquipmentFromDto(equipmentDto, equipment);

        // then
        assertEquals("New Generator", equipment.getName());
        assertEquals(EquipmentCategory.BACKUP_POWER, equipment.getCategory());
        assertEquals(new BigDecimal(1250), equipment.getPrice());
        assertEquals("New generator description", equipment.getDescription());
        assertEquals(EquipmentCondition.USED, equipment.getCondition());
    }
}