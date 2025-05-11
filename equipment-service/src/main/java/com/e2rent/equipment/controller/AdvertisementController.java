package com.e2rent.equipment.controller;


import com.e2rent.equipment.dto.AdvertisementDto;
import com.e2rent.equipment.dto.AdvertisementFilterDto;
import com.e2rent.equipment.dto.AdvertisementModerationRequestDto;
import com.e2rent.equipment.service.IAdvertisementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "REST APIs for Advertisement in E2Rent",
        description = "REST APIs in E2Rent to APPROVE, REJECT and VIEW advertisement details"
)
@RestController
@RequestMapping(path = "/advertisement/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
@RequiredArgsConstructor
@Validated
public class AdvertisementController {

    private final IAdvertisementService advertisementService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending")
    public ResponseEntity<Page<AdvertisementDto>> getAllPending(Pageable pageable) {
        Page<AdvertisementDto> pendingAds = advertisementService.getAllPending(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pendingAds);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveAdvertisement(@PathVariable Long id,
                                                     @Valid @RequestBody AdvertisementModerationRequestDto request) {
        advertisementService.approveAdvertisement(id, request.getAdminMessage());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectAdvertisement(@PathVariable Long id,
                                                    @Valid @RequestBody AdvertisementModerationRequestDto request) {
        advertisementService.rejectAdvertisement(id, request.getAdminMessage());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/approved/user/{userId}")
    public ResponseEntity<Page<AdvertisementDto>> getAllApprovedByUserId(@PathVariable Long userId,
                                                                         Pageable pageable) {
        Page<AdvertisementDto> approvedAds = advertisementService.getAllApprovedByUserId(userId, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(approvedAds);
    }

    @GetMapping("/my")
    public ResponseEntity<Page<AdvertisementDto>> fetchMyAdvertisements(
            Pageable pageable,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        Page<AdvertisementDto> myAds = advertisementService.getMyAdvertisements(authToken, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(myAds);
    }

    @GetMapping("/approved")
    public ResponseEntity<Page<AdvertisementDto>> searchAdvertisements(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
            @Valid AdvertisementFilterDto filter,
            Pageable pageable) {
        Page<AdvertisementDto> filteredAds = advertisementService.
                searchFilteredAdvertisements(authToken, filter, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(filteredAds);
    }

}
