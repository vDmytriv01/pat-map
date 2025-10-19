package com.vdmytriv.patmap.dto.place;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePlaceRequestDto {

    @NotBlank
    private String name;

    @NotNull
    private Long categoryId;

    private String address;

    private Double latitude;

    private Double longitude;

    private String description;

    private Double rating;

    private String openingHours;

    private String website;

    private String phone;
}
