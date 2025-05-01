package com.e2rent.rent_service.entity;

import com.e2rent.rent_service.enums.RentalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rental")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RentalStatus status;

    @Column(name = "owner_response_at")
    private LocalDateTime ownerResponseAt;

    @Column(name = "owner_message")
    private String ownerMessage;

    @Column(name = "equipment_id", nullable = false)
    private Long equipmentId;

    @Column(name = "renter_id", nullable = false)
    private Long renterId;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;
}
