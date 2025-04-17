package com.e2rent.equipment.mapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.e2rent.equipment.enums.EquipmentCategory;
import com.e2rent.equipment.enums.EquipmentSubcategory;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static com.e2rent.equipment.enums.EquipmentSubcategory.*;

class CategoryMapperTest {

    @Test
    @DisplayName("Test subcategories for BACKUP_POWER")
    void testBackupPowerSubcategories() {
        List<EquipmentSubcategory> result = CategoryMapper.getSubcategoriesFor(EquipmentCategory.BACKUP_POWER);
        assertEquals(List.of(DIESEL_GENERATOR, GAS_GENERATOR, INVERTER_GENERATOR, UPS, ENERGY_STORAGE), result);
    }

    @Test
    @DisplayName("Test subcategories for RENEWABLE_ENERGY")
    void testRenewableEnergySubcategories() {
        List<EquipmentSubcategory> result = CategoryMapper.getSubcategoriesFor(EquipmentCategory.RENEWABLE_ENERGY);
        assertEquals(List.of(SOLAR_PANEL, WIND_TURBINE, SOLAR_INVERTER, CHARGE_CONTROLLER), result);
    }

    @Test
    @DisplayName("Test subcategories for DISTRIBUTION")
    void testDistributionSubcategories() {
        List<EquipmentSubcategory> result = CategoryMapper.getSubcategoriesFor(EquipmentCategory.DISTRIBUTION);
        assertEquals(List.of(DISTRIBUTION_BOX, CIRCUIT_BREAKER, PHASE_SWITCH, RELAY), result);
    }

    @Test
    @DisplayName("Test subcategories for CABLING")
    void testCablingSubcategories() {
        List<EquipmentSubcategory> result = CategoryMapper.getSubcategoriesFor(EquipmentCategory.CABLING);
        assertEquals(List.of(EXTENSION_CORD, CABLE_DRUM, ADAPTER, GROUNDING_EQUIPMENT), result);
    }

    @Test
    @DisplayName("Test subcategories for INDUSTRIAL")
    void testIndustrialSubcategories() {
        List<EquipmentSubcategory> result = CategoryMapper.getSubcategoriesFor(EquipmentCategory.INDUSTRIAL);
        assertEquals(List.of(TRANSFORMER, VOLTAGE_STABILIZER, FREQUENCY_CONVERTER, ELECTRIC_COMPRESSOR), result);
    }

    @Test
    @DisplayName("Test subcategories for TOOLS")
    void testToolsSubcategories() {
        List<EquipmentSubcategory> result = CategoryMapper.getSubcategoriesFor(EquipmentCategory.TOOLS);
        assertEquals(List.of(CHARGING_STATION, WELDING_MACHINE, DC_TO_AC_INVERTER), result);
    }

    @Test
    @DisplayName("Test subcategories for MONITORING")
    void testMonitoringSubcategories() {
        List<EquipmentSubcategory> result = CategoryMapper.getSubcategoriesFor(EquipmentCategory.MONITORING);
        assertEquals(List.of(ENERGY_METER, SMART_SENSOR, SMART_HOME_DEVICE), result);
    }

    @Test
    @DisplayName("Test subcategories for OTHER")
    void testOtherSubcategories() {
        List<EquipmentSubcategory> result = CategoryMapper.getSubcategoriesFor(EquipmentCategory.OTHER);
        assertEquals(List.of(OTHER), result);
    }

    @Test
    @DisplayName("Test all categories are covered")
    void testAllCategoriesHandled() {
        for (EquipmentCategory category : EquipmentCategory.values()) {
            assertDoesNotThrow(() -> CategoryMapper.getSubcategoriesFor(category),
                    "Method should not throw for category: " + category);
        }
    }
}