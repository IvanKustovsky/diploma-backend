package com.example.equipment.controller;

import com.example.equipment.constants.EquipmentConstants;
import com.example.equipment.dto.EquipmentDto;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(
        name = "CRUD REST APIs for Equipment in E2Rent",
        description = "CRUD REST APIs in E2Rent to CREATE, FETCH, UPDATE AND DELETE equipment details"
)
@RestController
@RequestMapping(path = "/api/v1/equipment", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j // TODO: Add logging
@RequiredArgsConstructor
@Validated
public class EquipmentController {

    private final IEquipmentService equipmentService;

    @Operation(summary = "Create user REST API",
            description = "REST API to create new User inside E2Rent")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
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
    @PostMapping(path = "/register", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDto> registerEquipment(
            @RequestPart("equipmentDto") @Valid EquipmentDto equipmentDto,
            @RequestParam(value = "main-image", required = false) MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            equipmentService.registerEquipment(equipmentDto);
        } else {
            equipmentService.registerEquipmentWithMainImage(equipmentDto, file);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(EquipmentConstants.STATUS_201, EquipmentConstants.MESSAGE_201));
    }

    @Operation(summary = "Fetch equipment REST API",
            description = "REST API to fetch Equipment inside E2Rent")
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
    @GetMapping("/fetch/{id}")
    public ResponseEntity<EquipmentDto> fetchEquipment(@PathVariable Long id) {
        EquipmentDto equipmentDto = equipmentService.fetchEquipment(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(equipmentDto);
    }
}
