package com.e2rent.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_unique_email", columnList = "email", unique = true),
                @Index(name = "idx_unique_company_id", columnList = "company_id", unique = true),
                @Index(name = "idx_unique_mobile_number", columnList = "mobile_number", unique = true),
                @Index(name = "idx_unique_password", columnList = "password", unique = true)
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

    @Column(unique = true, nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "fk_user_roles_user")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "roleId",
                    foreignKey = @ForeignKey(name = "fk_user_roles_role")
            )
    )
    private List<Role> roles = new ArrayList<>();

    public void deleteRoles() {
        this.roles.clear();
    }
}
