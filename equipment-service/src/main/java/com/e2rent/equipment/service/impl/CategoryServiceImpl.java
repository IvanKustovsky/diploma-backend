package com.e2rent.equipment.service.impl;

import com.e2rent.equipment.enums.EquipmentCategory;
import com.e2rent.equipment.enums.EquipmentSubcategory;
import com.e2rent.equipment.mapper.CategoryMapper;
import com.e2rent.equipment.service.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Override
    public Map<EquipmentCategory, List<EquipmentSubcategory>> getAllCategoriesWithSubcategories() {
        return Arrays.stream(EquipmentCategory.values())
                .collect(Collectors.toMap(
                        category -> category,
                        CategoryMapper::getSubcategoriesFor
                ));
    }
}
