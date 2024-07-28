package com.example.user_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId; // TODO: Change companyId and roleId to @OneToOne relationship

    private Long roleId;

    private boolean isCompany;

    private String fullName;

    private String email;

    private String mobileNumber;

    private String password;
}
