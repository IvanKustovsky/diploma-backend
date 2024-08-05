package com.example.user_service.service.impl;

import com.example.user_service.entity.Role;
import com.example.user_service.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl {

    private final RoleRepository roleRepository;

    private final List<Role> roles = List.of(
            Role.builder().name("USER").build(),
            Role.builder().name("ADMIN").build(),
            Role.builder().name("MANAGER").build());

    @PostConstruct
    @Transactional
    public void init() {
        if (roleRepository.count() == 0) {
            roleRepository.saveAll(roles);
        }
    }
}
