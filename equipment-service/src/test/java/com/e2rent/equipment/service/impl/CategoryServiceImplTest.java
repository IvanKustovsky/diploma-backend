package com.e2rent.equipment.service.impl;

import com.e2rent.equipment.enums.EquipmentCategory;
import com.e2rent.equipment.enums.EquipmentSubcategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Category Service Test Class")
class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Test getAllCategoriesWithSubcategories returns correct mapping")
    void testGetAllCategoriesWithSubcategories() {
        Map<EquipmentCategory, List<EquipmentSubcategory>> result = categoryService.getAllCategoriesWithSubcategories();

        // 1. Ensure map contains all categories
        assertEquals(EquipmentCategory.values().length, result.size(),
                "Map should contain all EquipmentCategory values");

        // 2. Ensure no nulls
        result.forEach((category, subcategories) -> {
            assertNotNull(subcategories, "Subcategories list should not be null for category: " + category);
            assertFalse(subcategories.isEmpty(), "Subcategories list should not be empty for category: " + category);
        });

        // 3. Optional: Check one concrete mapping
        assertTrue(result.get(EquipmentCategory.BACKUP_POWER).contains(EquipmentSubcategory.DIESEL_GENERATOR),
                "BACKUP_POWER should contain DIESEL_GENERATOR");
    }
}