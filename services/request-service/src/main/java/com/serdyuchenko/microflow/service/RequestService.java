package com.serdyuchenko.microflow.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.serdyuchenko.microflow.client.NotificationClient;
import com.serdyuchenko.microflow.dto.CreateRequestDto;
import com.serdyuchenko.microflow.dto.CreateRequestResponse;

@Service
public class RequestService {
    private static final Logger log = LoggerFactory.getLogger(RequestService.class);

    private final NotificationClient notificationClient;

    public RequestService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public CreateRequestResponse create(CreateRequestDto dto) {
        UUID requestId = UUID.randomUUID();

        log.info("Request created: requestId={}, text={}", requestId, dto.text());
        notificationClient.send(requestId);

        return new CreateRequestResponse(requestId, "created");
    }
}
