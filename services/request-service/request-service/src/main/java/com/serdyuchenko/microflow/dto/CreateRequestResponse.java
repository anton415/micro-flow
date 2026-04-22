package com.serdyuchenko.microflow.dto;

import java.util.UUID;

public record CreateRequestResponse(
    UUID requestId,
    String status
) {
}
