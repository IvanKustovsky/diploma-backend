package com.e2rent.rent_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRentalRequestDto {

    @NotNull(message = "ID обладнання не може бути порожнім")
    @Positive(message = "ID обладнання має бути додатнім числом")
    private Long equipmentId;

    @NotNull(message = "Дата початку не може бути порожньою")
    @FutureOrPresent(message = "Дата початку не може бути в минулому")
    private LocalDate startDate;

    @NotNull(message = "Дата завершення не може бути порожньою")
    private LocalDate endDate;

    @NotBlank(message = "Адреса не може бути порожньою")
    private String address;

    @AssertTrue(message = "Дата завершення має бути не раніше дати початку")
    public boolean isEndDateValid() {
        return startDate != null && endDate != null && !endDate.isBefore(startDate);
    }
}
