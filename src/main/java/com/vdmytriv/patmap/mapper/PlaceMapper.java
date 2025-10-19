package com.vdmytriv.patmap.mapper;

import com.vdmytriv.patmap.dto.place.CreatePlaceRequestDto;
import com.vdmytriv.patmap.dto.place.PlaceDto;
import com.vdmytriv.patmap.dto.place.UpdatePlaceRequestDto;
import com.vdmytriv.patmap.model.Place;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface PlaceMapper {

    PlaceDto toDto(Place place);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    Place toEntity(CreatePlaceRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    void updatePlaceFromDto(UpdatePlaceRequestDto dto, @MappingTarget Place place);
}
