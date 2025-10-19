package com.vdmytriv.patmap.service.impl;

import com.vdmytriv.patmap.dto.place.CreatePlaceRequestDto;
import com.vdmytriv.patmap.dto.place.PlaceDto;
import com.vdmytriv.patmap.dto.place.UpdatePlaceRequestDto;
import com.vdmytriv.patmap.mapper.PlaceMapper;
import com.vdmytriv.patmap.model.Category;
import com.vdmytriv.patmap.model.Place;
import com.vdmytriv.patmap.repository.CategoryRepository;
import com.vdmytriv.patmap.repository.PlaceRepository;
import com.vdmytriv.patmap.service.PlaceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final CategoryRepository categoryRepository;
    private final PlaceMapper placeMapper;

    @Override
    @Transactional
    public PlaceDto create(CreatePlaceRequestDto requestDto) {
        Place place = placeMapper.toEntity(requestDto);
        place.setCategory(getCategory(requestDto.getCategoryId()));
        Place saved = placeRepository.save(place);
        return placeMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaceDto> getAll() {
        return placeRepository.findAll().stream()
                .map(placeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaceDto> search(Long categoryId, String city) {
        List<Place> places;
        if (categoryId != null && city != null && !city.isBlank()) {
            places = placeRepository.findAllByCategoryIdAndAddressContainingIgnoreCase(categoryId, city);
        } else if (categoryId != null) {
            places = placeRepository.findAllByCategoryId(categoryId);
        } else if (city != null && !city.isBlank()) {
            places = placeRepository.findAllByAddressContainingIgnoreCase(city);
        } else {
            return getAll();
        }
        return places.stream()
                .map(placeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PlaceDto getById(Long id) {
        Place place = getPlace(id);
        return placeMapper.toDto(place);
    }

    @Override
    @Transactional
    public PlaceDto update(Long id, UpdatePlaceRequestDto requestDto) {
        Place place = getPlace(id);
        placeMapper.updatePlaceFromDto(requestDto, place);
        if (requestDto.getCategoryId() != null
                && (place.getCategory() == null
                || !requestDto.getCategoryId().equals(place.getCategory().getId()))) {
            place.setCategory(getCategory(requestDto.getCategoryId()));
        }
        Place updated = placeRepository.save(place);
        return placeMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Place place = getPlace(id);
        placeRepository.delete(place);
    }

    private Place getPlace(Long id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Place not found with id: " + id));
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }
}
