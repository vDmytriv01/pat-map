package com.vdmytriv.patmap.dto.place;

import com.vdmytriv.patmap.dto.category.CategoryDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlaceDto {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private CategoryDto category;

    private String address;

    private Double latitude;

    private Double longitude;

    private String description;

    private Double rating;

    private String openingHours;

    private String website;

    private String phone;
}
