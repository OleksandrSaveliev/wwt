package com.wwt.dataapi.controller;

import com.wwt.dataapi.dto.TransformRequest;
import com.wwt.dataapi.dto.TransformResponse;
import com.wwt.dataapi.service.TransformService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class TransformController {

    private final TransformService transformService;

    @PostMapping("transform")
    public ResponseEntity<TransformResponse> transform(@Valid @RequestBody TransformRequest body) {
        TransformResponse transformedText = transformService.transform(body.text());
        return ResponseEntity.ok(transformedText);
    }
}
