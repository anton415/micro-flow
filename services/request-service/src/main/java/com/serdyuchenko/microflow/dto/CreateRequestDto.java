package com.serdyuchenko.microflow.dto;

import javax.validation.constraints.NotBlank;

public record CreateRequestDto(
    @NotBlank String text
) {
}
