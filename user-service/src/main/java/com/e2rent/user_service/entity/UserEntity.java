package com.e2rent.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Company.class)
    @JoinColumn(name = "company_id", referencedColumnName = "companyId")
    // TODO consider using @Embedded and @Embeddable to make it value object(not entity); also @Index
    private Company company;

    @Column(nullable = false, name = "full_name")
    private String fullName;

    // @NaturalId // TODO Check if needed
    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false, name = "mobile_number")
    private String mobileNumber;

    @Column(unique = true, nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "roleId"))
    private List<Role> roles = new ArrayList<>();

    public void deleteRoles() {
        this.roles.clear();
    }
}
