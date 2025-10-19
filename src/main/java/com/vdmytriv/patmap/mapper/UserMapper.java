package com.vdmytriv.patmap.mapper;

import com.vdmytriv.patmap.dto.user.UserDto;
import com.vdmytriv.patmap.model.Role;
import com.vdmytriv.patmap.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", expression = "java(mapRoleNames(user.getRoles()))")
    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "favoritePlaces", ignore = true)
    User toEntity(UserDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "favoritePlaces", ignore = true)
    void updateUserFromDto(UserDto dto, @MappingTarget User user);

    default Set<String> mapRoleNames(Set<Role> roles) {
        return roles == null ? null : roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}
