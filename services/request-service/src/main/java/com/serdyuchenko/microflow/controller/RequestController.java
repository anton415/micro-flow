package com.serdyuchenko.microflow.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.serdyuchenko.microflow.dto.CreateRequestDto;
import com.serdyuchenko.microflow.dto.CreateRequestResponse;
import com.serdyuchenko.microflow.service.RequestService;

/**
 * Принимает HTTP-запросы. 
 */
@RestController
@RequestMapping("/requests")
public class RequestController {
    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateRequestResponse create(@Valid @RequestBody CreateRequestDto dto) {
        return requestService.create(dto);
    }
}
