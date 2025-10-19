package com.vdmytriv.patmap.dto.booking;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateBookingRequestDto {

    @NotNull
    private Long placeId;

    @NotNull
    @FutureOrPresent
    private LocalDateTime dateTime;
}
