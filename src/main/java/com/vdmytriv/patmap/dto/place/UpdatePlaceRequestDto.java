package com.vdmytriv.patmap.dto.place;

import lombok.Data;

@Data
public class UpdatePlaceRequestDto {

    private String name;

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
