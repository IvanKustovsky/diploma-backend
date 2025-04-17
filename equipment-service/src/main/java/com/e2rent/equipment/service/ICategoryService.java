package com.e2rent.equipment.service;

import com.e2rent.equipment.enums.EquipmentCategory;
import com.e2rent.equipment.enums.EquipmentSubcategory;

import java.util.List;
import java.util.Map;

public interface ICategoryService {

    Map<EquipmentCategory, List<EquipmentSubcategory>> getAllCategoriesWithSubcategories();
}
