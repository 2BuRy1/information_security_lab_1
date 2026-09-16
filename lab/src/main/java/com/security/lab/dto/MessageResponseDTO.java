package com.security.lab.dto;

import com.security.lab.serializer.XssStringSerializer;
import java.time.LocalDateTime;
import tools.jackson.databind.annotation.JsonSerialize;

public record MessageResponseDTO(
        Long id,
        @JsonSerialize(using = XssStringSerializer.class) String from,
        @JsonSerialize(using = XssStringSerializer.class) String to,
        @JsonSerialize(using = XssStringSerializer.class) String text,
        LocalDateTime createdAt) {}
