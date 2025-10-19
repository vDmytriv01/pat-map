package com.vdmytriv.patmap.service;

import com.vdmytriv.patmap.dto.place.CreatePlaceRequestDto;
import com.vdmytriv.patmap.dto.place.PlaceDto;
import com.vdmytriv.patmap.dto.place.UpdatePlaceRequestDto;

import java.util.List;

public interface PlaceService {

    PlaceDto create(CreatePlaceRequestDto requestDto);

    List<PlaceDto> getAll();

    List<PlaceDto> search(Long categoryId, String city);

    PlaceDto getById(Long id);

    PlaceDto update(Long id, UpdatePlaceRequestDto requestDto);

    void delete(Long id);
}
