package com.e2rent.equipment.controller;

import com.e2rent.equipment.constants.EquipmentConstants;
import com.e2rent.equipment.dto.EquipmentDto;
import com.e2rent.equipment.dto.EquipmentSummaryDto;
import com.e2rent.equipment.dto.ErrorResponseDto;
import com.e2rent.equipment.dto.ResponseDto;
import com.e2rent.equipment.enums.EquipmentCategory;
import com.e2rent.equipment.enums.EquipmentSubcategory;
import com.e2rent.equipment.service.ICategoryService;
import com.e2rent.equipment.service.IEquipmentService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(
        name = "REST APIs for Equipment in E2Rent",
        description = "REST APIs in E2Rent to CREATE, FETCH, UPDATE AND DELETE equipment details"
)
@RestController
@RequestMapping(path = "/equipments/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
@RequiredArgsConstructor
@Validated
public class EquipmentController {

    private final IEquipmentService equipmentService;
    private final ICategoryService categoryService;

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
            @RequestParam(value = "main-image", required = false) MultipartFile image,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken) {
        equipmentService.registerEquipment(equipmentDto, image, authorizationToken);
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
            @PathVariable(value = "id") @Positive(message = "Equipment id must be positive number") Long id) {
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
            @PathVariable(value = "id") @Positive(message = "Equipment id must be positive number") Long id,
            @RequestPart("equipmentDto") @Valid EquipmentDto equipmentDto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken) {
        equipmentService.updateEquipment(id, equipmentDto, authorizationToken);
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
            @PathVariable(value = "id") @Positive(message = "Equipment id must be positive number") Long id) {
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
    public ResponseEntity<Page<EquipmentSummaryDto>> fetchEquipments(Pageable pageable) {
        var equipments = equipmentService.findAllEquipmentsWithImage(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(equipments);
    }

    @Operation(summary = "Fetch user equipments REST API",
            description = "REST API to fetch user Equipments with main image uploaded inside E2Rent")
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
    @GetMapping("/my")
    public ResponseEntity<Page<EquipmentSummaryDto>> fetchMyEquipments(Pageable pageable,
                                                                       @RequestHeader(HttpHeaders.AUTHORIZATION)
                                                                       String authorizationToken) {
        var equipments = equipmentService.findEquipmentsByUser(authorizationToken, pageable);
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
    public ResponseEntity<ResponseDto> uploadMainImage(
            @PathVariable(value = "id") Long id,
            @RequestParam("main-image") MultipartFile image,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken) {
        equipmentService.uploadMainImage(id, image, authorizationToken);
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
    public ResponseEntity<ResponseDto> uploadImages(@PathVariable(value = "id") Long id,
                                                    @RequestParam("images") List<MultipartFile> images,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken) {
        equipmentService.uploadImages(id, images, authorizationToken);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(EquipmentConstants.STATUS_200, EquipmentConstants.MESSAGE_200));
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long id) {
        byte[] imageData = equipmentService.downloadImage(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .body(imageData);
    }

    @GetMapping("/categories-with-subcategories")
    public ResponseEntity<Map<EquipmentCategory, List<EquipmentSubcategory>>> getCategoriesWithSubcategories() {
        var categoriesWithSubCategories = categoryService.getAllCategoriesWithSubcategories();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoriesWithSubCategories);
    }
}
