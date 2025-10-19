package com.vdmytriv.patmap.service;

import com.vdmytriv.patmap.dto.place.PlaceDto;
import com.vdmytriv.patmap.dto.user.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto dto);

    List<UserDto> getAll();

    UserDto getById(Long id);

    UserDto getByEmail(String email);

    UserDto update(Long id, UserDto dto);

    void delete(Long id);

    List<PlaceDto> getFavoritePlaces(Long userId);

    void addFavoritePlace(Long userId, Long placeId);

    void removeFavoritePlace(Long userId, Long placeId);
}
