package com.security.lab.dto;

import com.security.lab.serializer.XssStringSerializer;
import tools.jackson.databind.annotation.JsonSerialize;

public record UserResponseDTO(
        Long id, @JsonSerialize(using = XssStringSerializer.class) String login) {}
