package com.example.equipment.controller;

import com.example.equipment.constants.EquipmentConstants;
import com.example.equipment.dto.EquipmentDto;
import com.example.equipment.dto.EquipmentSummaryDto;
import com.example.equipment.dto.ErrorResponseDto;
import com.example.equipment.dto.ResponseDto;
import com.example.equipment.service.IEquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(
        name = "REST APIs for Equipment in E2Rent",
        description = "REST APIs in E2Rent to CREATE, FETCH, UPDATE AND DELETE equipment details"
)
@RestController
@RequestMapping(path = "/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j // TODO: Add logging
@RequiredArgsConstructor
@Validated
public class EquipmentController {

    private final IEquipmentService equipmentService;

    @Operation(summary = "Register equipment REST API",
            description = "REST API to register new Equipment inside E2Rent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status BAD_REQUEST"
            ),
            @ApiResponse(
                    responseCode = "415",
                    description = "HTTP Status UNSUPPORTED_MEDIA_TYPE"
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
    @PostMapping(path = "/register")
    public ResponseEntity<ResponseDto> registerEquipment(
            @RequestPart("equipmentDto") @Valid EquipmentDto equipmentDto,
            @RequestParam(value = "main-image", required = false) MultipartFile image) {
        equipmentService.registerEquipment(equipmentDto, image);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(EquipmentConstants.STATUS_201, EquipmentConstants.MESSAGE_201));
    }

    @Operation(summary = "Fetch equipment REST API",
            description = "REST API to fetch Equipment by its ID inside E2Rent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status NOT_FOUND"
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
    @GetMapping("/{id}")
    public ResponseEntity<EquipmentDto> fetchEquipment(
            @PathVariable @Positive(message = "Equipment id must be positive number") Long id) {
        EquipmentDto equipmentDto = equipmentService.fetchEquipment(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(equipmentDto);
    }

    @Operation(summary = "Update equipment REST API",
            description = "REST API to update Equipment by its ID inside E2Rent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status BAD_REQUEST"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status NOT_FOUND"
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
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateEquipmentDetails(
            @PathVariable @Positive(message = "Equipment id must be positive number") Long id,
            @RequestPart("equipmentDto") @Valid EquipmentDto equipmentDto,
            @RequestParam(value = "main-image", required = false) MultipartFile image) {
        equipmentService.updateEquipment(id, equipmentDto, image);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(EquipmentConstants.STATUS_200, EquipmentConstants.MESSAGE_200));
    }

    @Operation(summary = "Delete equipment REST API",
            description = "REST API to delete Equipment by its ID inside E2Rent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status NOT_FOUND"
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
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteEquipmentDetails(
            @PathVariable @Positive(message = "Equipment id must be positive number") Long id) {
        equipmentService.deleteEquipment(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(EquipmentConstants.STATUS_200, EquipmentConstants.MESSAGE_200));
    }

    @Operation(summary = "Fetch equipments REST API",
            description = "REST API to fetch all Equipments with main image uploaded inside E2Rent")
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
    @GetMapping("/all")
    public ResponseEntity<List<EquipmentSummaryDto>> fetchEquipment() {
        var equipments = equipmentService.findAllEquipmentsWithImage();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(equipments);
    }

    @Operation(summary = "Upload Main Image REST API",
            description = "REST API to upload main image of the equipment inside E2Rent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status BAD_REQUEST"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status NOT_FOUND"
            ),
            @ApiResponse(
                    responseCode = "415",
                    description = "HTTP Status UNSUPPORTED_MEDIA_TYPE"
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
    @PostMapping(value = "/{id}/images/main")
    public ResponseEntity<ResponseDto> uploadMainImage(@PathVariable Long id,
                                                       @RequestParam("main-image") MultipartFile image) {
        equipmentService.uploadMainImage(id, image);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(EquipmentConstants.STATUS_200, EquipmentConstants.MESSAGE_200));

    }

    @Operation(summary = "Upload images REST API",
            description = "REST API to upload images of the equipment inside E2Rent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status BAD_REQUEST"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status NOT_FOUND"
            ),
            @ApiResponse(
                    responseCode = "415",
                    description = "HTTP Status UNSUPPORTED_MEDIA_TYPE"
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
    @PostMapping("/{id}/images")
    public ResponseEntity<ResponseDto> uploadImages(@PathVariable Long id,
                                                    @RequestParam("images") List<MultipartFile> images) {
        equipmentService.uploadImages(id, images);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(EquipmentConstants.STATUS_200, EquipmentConstants.MESSAGE_200));

    }
}
