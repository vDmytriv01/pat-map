package com.vdmytriv.patmap.service.impl;

import com.vdmytriv.patmap.dto.user.UserDto;
import com.vdmytriv.patmap.mapper.UserMapper;
import com.vdmytriv.patmap.model.Role;
import com.vdmytriv.patmap.model.User;
import com.vdmytriv.patmap.repository.RoleRepository;
import com.vdmytriv.patmap.repository.UserRepository;
import com.vdmytriv.patmap.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto create(UserDto dto) {
        User user = userMapper.toEntity(dto);
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
    @Transactional
    public UserDto update(Long id, UserDto dto) {
        User user = getUser(id);
        userMapper.updateUserFromDto(dto, user);
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

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
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
