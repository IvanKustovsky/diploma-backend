package com.e2rent.rent_service.controller;

import com.e2rent.rent_service.constants.RentalConstants;
import com.e2rent.rent_service.dto.CreateRentalRequestDto;
import com.e2rent.rent_service.dto.RejectRentalRequestDto;
import com.e2rent.rent_service.dto.RentalResponseDto;
import com.e2rent.rent_service.dto.ResponseDto;
import com.e2rent.rent_service.service.IRentalService;
import jakarta.validation.Valid;
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


@RestController
@RequestMapping(path = "/rentals/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
@RequiredArgsConstructor
@Validated
public class RentalController {

    private final IRentalService rentalService;

    @PostMapping
    public ResponseEntity<ResponseDto> createRental(@Valid @RequestBody CreateRentalRequestDto createRentalRequestDto,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        rentalService.createRental(createRentalRequestDto, authToken);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(RentalConstants.STATUS_201, RentalConstants.MESSAGE_201));
    }

    @GetMapping("/{rentalId}")
    public ResponseEntity<RentalResponseDto> getRentalById(@PathVariable Long rentalId,
                                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        RentalResponseDto rental = rentalService.getRentalById(rentalId, authToken);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(rental);
    }

    @GetMapping("/outgoing")
    public ResponseEntity<Page<RentalResponseDto>> getMyOutgoingRentals(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken, Pageable pageable) {

        var myOutgoingRentals = rentalService.getMyOutgoingRentals(authToken, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(myOutgoingRentals);
    }

    @GetMapping("/incoming")
    public ResponseEntity<Page<RentalResponseDto>> getMyIncomingRentals(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken, Pageable pageable) {

        var myIncomingRentals = rentalService.getMyIncomingRentals(authToken, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(myIncomingRentals);
    }

    @PutMapping("/{rentalId}/approve")
    public ResponseEntity<ResponseDto> approveRental(@PathVariable Long rentalId,
                                                     @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        rentalService.approveRental(rentalId, authToken);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(RentalConstants.STATUS_200, RentalConstants.MESSAGE_200));
    }

    @PutMapping("/{rentalId}/reject")
    public ResponseEntity<ResponseDto> rejectRental(@PathVariable Long rentalId,
                                                    @Valid @RequestBody RejectRentalRequestDto rejectRentalRequestDto,
                                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        rentalService.rejectRental(rentalId, rejectRentalRequestDto.getRejectionMessage(), authToken);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(RentalConstants.STATUS_200, RentalConstants.STATUS_200));
    }

}
