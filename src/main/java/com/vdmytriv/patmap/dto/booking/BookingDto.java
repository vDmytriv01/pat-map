package com.vdmytriv.patmap.dto.booking;

import com.vdmytriv.patmap.model.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDto {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long placeId;

    @NotNull
    private LocalDateTime dateTime;

    @NotNull
    private BookingStatus status;
}
