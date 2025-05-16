package com.e2rent.equipment.mapper;

import com.e2rent.equipment.enums.EquipmentCategory;
import com.e2rent.equipment.enums.EquipmentSubcategory;

import java.util.List;

import static com.e2rent.equipment.enums.EquipmentSubcategory.*;

public class CategoryMapper {

    public static List<EquipmentSubcategory> getSubcategoriesFor(EquipmentCategory category) {
        return switch (category) {
            case BACKUP_POWER -> List.of(
                    DIESEL_GENERATOR, GAS_GENERATOR, INVERTER_GENERATOR, UPS, ENERGY_STORAGE, OTHER
            );
            case RENEWABLE_ENERGY -> List.of(
                    SOLAR_PANEL, WIND_TURBINE, SOLAR_INVERTER, CHARGE_CONTROLLER, OTHER
            );
            case DISTRIBUTION -> List.of(
                    DISTRIBUTION_BOX, CIRCUIT_BREAKER, PHASE_SWITCH, RELAY, OTHER
            );
            case CABLING -> List.of(
                    EXTENSION_CORD, CABLE_DRUM, ADAPTER, GROUNDING_EQUIPMENT, OTHER
            );
            case INDUSTRIAL -> List.of(
                    TRANSFORMER, VOLTAGE_STABILIZER, FREQUENCY_CONVERTER, ELECTRIC_COMPRESSOR, OTHER
            );
            case TOOLS -> List.of(
                    CHARGING_STATION, WELDING_MACHINE, DC_TO_AC_INVERTER, OTHER
            );
            case MONITORING -> List.of(
                    ENERGY_METER, SMART_SENSOR, SMART_HOME_DEVICE, OTHER
            );
            case OTHER -> List.of(OTHER);
        };
    }
}

