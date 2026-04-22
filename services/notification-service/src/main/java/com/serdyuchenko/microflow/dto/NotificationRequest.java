package com.serdyuchenko.microflow.dto;

import java.util.UUID;

import javax.validation.constraints.NotNull;

public record NotificationRequest(
    @NotNull UUID requestId
) {
}
