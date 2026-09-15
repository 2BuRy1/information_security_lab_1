package com.security.lab.controller;

import com.security.lab.dto.UserResponseDTO;
import com.security.lab.repository.UserRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SecurityLabController {

    private final UserRepository userRepository;

    public SecurityLabController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/data")
    public List<UserResponseDTO> data() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getUsername()))
                .toList();
    }
}
