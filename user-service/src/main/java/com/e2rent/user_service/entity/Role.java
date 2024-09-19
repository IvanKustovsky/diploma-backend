package com.e2rent.user_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role", indexes = {
        @Index(name = "idx_unique_role_name", columnList = "name", unique = true)
})
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int roleId;

    @Column(nullable = false, unique = true, name = "name")
    private String name;
}
