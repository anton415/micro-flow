package com.serdyuchenko.microflow.client;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.serdyuchenko.microflow.dto.NotificationRequest;

@Component
public class NotificationClient {
    private static final Logger log = LoggerFactory.getLogger(NotificationClient.class);

    private final RestTemplate restTemplate;
    private final String notificationServiceUrl;

    public NotificationClient(RestTemplate restTemplate,
                              @Value("${notification.service.url}") String notificationServiceUrl) {
        this.restTemplate = restTemplate;
        this.notificationServiceUrl = notificationServiceUrl;
    }

    public void send(UUID requestId) {
        try {
            restTemplate.postForEntity(
                    notificationServiceUrl + "/notifications",
                    new NotificationRequest(requestId),
                    Void.class
            );
            log.info("Notification sent: requestId={}", requestId);
        } catch (RestClientException ex) {
            log.warn("Notification send failed: requestId={}", requestId, ex);
        }
    }
}
