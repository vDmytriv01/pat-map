package com.vdmytriv.patmap.dto.common;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class ErrorResponseDto {

    Instant timestamp;
    int status;
    String message;
    String path;
}
