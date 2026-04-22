package com.serdyuchenko.microflow.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.serdyuchenko.microflow.dto.NotificationRequest;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void process(NotificationRequest request) {
        log.info("Notification received: requestId={}", request.requestId());
    }
}
