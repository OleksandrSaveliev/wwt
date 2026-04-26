package com.wwt.dataapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TransformRequest(
        @NotBlank(message = "Text must not be blank")
        @Size(max = 1000, message = "Text must not exceed 1000 characters")
        String text
) {}