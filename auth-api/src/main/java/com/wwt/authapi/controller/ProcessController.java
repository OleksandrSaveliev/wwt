package com.wwt.authapi.controller;

import com.wwt.authapi.dto.ProcessRequest;
import com.wwt.authapi.dto.ProcessResponse;
import com.wwt.authapi.service.ProcessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProcessController {

    private final ProcessService processService;

    @PostMapping("/process")
    public ResponseEntity<ProcessResponse> process(@Valid @RequestBody ProcessRequest request) {
        ProcessResponse response = processService.processRequest(request);
        return ResponseEntity.ok(response);
    }
}