package com.security.lab.dto;

import java.time.LocalDateTime;

public record MessageResponseDTO(
        Long id, String from, String to, String text, LocalDateTime createdAt) {}
