package com.e2rent.equipment.controller;


import com.e2rent.equipment.dto.AdvertisementDto;
import com.e2rent.equipment.dto.AdvertisementModerationRequestDto;
import com.e2rent.equipment.dto.ErrorResponseDto;
import com.e2rent.equipment.service.IAdvertisementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Operation(summary = "Approve advertisement",
            description = "Approve an advertisement by ID and leave admin comment")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveAdvertisement(@PathVariable Long id,
                                                     @Valid @RequestBody AdvertisementModerationRequestDto request) {
        advertisementService.approveAdvertisement(id, request.getAdminMessage());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reject advertisement",
            description = "Reject an advertisement by ID and leave admin comment")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectAdvertisement(@PathVariable Long id,
                                                    @Valid @RequestBody AdvertisementModerationRequestDto request) {
        advertisementService.rejectAdvertisement(id, request.getAdminMessage());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Fetch advertisements REST API",
            description = "REST API to fetch all approved advertisements inside E2Rent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/approved")
    public ResponseEntity<Page<AdvertisementDto>> getAllApproved(Pageable pageable) {
        Page<AdvertisementDto> approvedAds = advertisementService.getAllApproved(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(approvedAds);
    }

    @Operation(summary = "Fetch advertisements by user ID REST API",
            description = "REST API to fetch all approved advertisements by user ID inside E2Rent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/approved/user/{userId}")
    public ResponseEntity<Page<AdvertisementDto>> getAllApprovedByUserId(@PathVariable Long userId,
                                                                         Pageable pageable) {
        Page<AdvertisementDto> approvedAds = advertisementService.getAllApprovedByUserId(userId, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(approvedAds);
    }
}
