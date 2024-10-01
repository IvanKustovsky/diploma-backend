package com.e2rent.user_service.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_unique_email", columnList = "email", unique = true),
                @Index(name = "idx_unique_company_id", columnList = "company_id", unique = true),
                @Index(name = "idx_unique_mobile_number", columnList = "mobile_number", unique = true)
        }
)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Company.class)
    @JoinColumn(name = "company_id", referencedColumnName = "companyId",
            foreignKey = @ForeignKey(name = "fk_users_company"))
    private Company company;

    @Column(nullable = false, name = "full_name")
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false, name = "mobile_number")
    private String mobileNumber;
}
