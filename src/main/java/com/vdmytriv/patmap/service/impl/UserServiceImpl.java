package com.vdmytriv.patmap.service.impl;

import com.vdmytriv.patmap.dto.place.PlaceDto;
import com.vdmytriv.patmap.dto.user.UserDto;
import com.vdmytriv.patmap.mapper.PlaceMapper;
import com.vdmytriv.patmap.mapper.UserMapper;
import com.vdmytriv.patmap.model.Place;
import com.vdmytriv.patmap.model.Role;
import com.vdmytriv.patmap.model.User;
import com.vdmytriv.patmap.repository.PlaceRepository;
import com.vdmytriv.patmap.repository.RoleRepository;
import com.vdmytriv.patmap.repository.UserRepository;
import com.vdmytriv.patmap.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PlaceMapper placeMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto create(UserDto dto) {
        User user = userMapper.toEntity(dto);
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setRoles(resolveRoles(dto.getRoles()));
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        User user = getUser(id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto dto) {
        User user = getUser(id);
        userMapper.updateUserFromDto(dto, user);
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getRoles() != null) {
            user.setRoles(resolveRoles(dto.getRoles()));
        }
        User updated = userRepository.save(user);
        return userMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = getUser(id);
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaceDto> getFavoritePlaces(Long userId) {
        User user = getUser(userId);
        return user.getFavoritePlaces().stream()
                .map(placeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void addFavoritePlace(Long userId, Long placeId) {
        User user = getUser(userId);
        Place place = getPlace(placeId);
        user.getFavoritePlaces().add(place);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeFavoritePlace(Long userId, Long placeId) {
        User user = getUser(userId);
        Place place = getPlace(placeId);
        user.getFavoritePlaces().remove(place);
        userRepository.save(user);
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    private Place getPlace(Long id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Place not found with id: " + id));
    }

    private Set<Role> resolveRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return new HashSet<>();
        }
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new EntityNotFoundException("Role not found with name: " + roleName));
            roles.add(role);
        }
        return roles;
    }
}
