package com.vdmytriv.patmap.service;

import com.vdmytriv.patmap.dto.user.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto dto);

    List<UserDto> getAll();

    UserDto getById(Long id);

    UserDto update(Long id, UserDto dto);

    void delete(Long id);
}
