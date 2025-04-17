package com.e2rent.equipment.entity;

import com.e2rent.equipment.enums.EquipmentCategory;
import com.e2rent.equipment.enums.EquipmentCondition;
import com.e2rent.equipment.enums.EquipmentStatus;
import com.e2rent.equipment.enums.EquipmentSubcategory;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipment", indexes = {
        @Index(name = "idx_unique_main_image_id", columnList = "main_image_id", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipmentId;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentSubcategory subcategory;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentCondition condition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentStatus status;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "main_image_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_equipment_main_image_id"))
    private Image mainImage;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private Long userId;

    public void addImage(Image image) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        if (image != null) {
            this.images.add(image);
            image.setEquipment(this);
        }
    }
}
