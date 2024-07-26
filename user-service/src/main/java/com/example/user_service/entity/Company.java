package com.example.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class Company extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "companyName")
    private String name;

    @Column(name = "companyCode")
    private String code;

    @Column(name = "companyContactNumber")
    private String contactNumber;

    private String address;
}
