package com.example.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;

    private Long roleId;

    private boolean isCompany;

    private String fullName;

    private String email;

    @Column(name = "userMobileNumber")
    private String mobileNumber;

    private String password;
}
