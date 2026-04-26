package com.wwt.authapi.service.impl;

import com.wwt.authapi.dto.*;
import com.wwt.authapi.entity.ProcessingLog;
import com.wwt.authapi.entity.User;
import com.wwt.authapi.repository.ProcessingLogRepository;
import com.wwt.authapi.repository.UserRepository;
import com.wwt.authapi.service.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {

    private final ProcessingLogRepository logRepository;
    private final UserRepository userRepository;
    private final RestClient restClient;

    @Value("${INTERNAL_TOKEN}")
    private String internalToken;

    @Value("${SERVICE_B_URL:http://data-api:8081}")
    private String serviceBUrl;

    @Transactional
    public ProcessResponse processRequest(ProcessRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User context not found"));

        ProcessResponse bResponse = restClient.post()
                .uri(serviceBUrl + "/api/transform")
                .header("X-Internal-Token", internalToken)
                .body(new ProcessRequest(request.text()))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (req, res) -> {
                    log.error("Service B returned error: {}", res.getStatusCode());
                    throw new RuntimeException("External transformation service failed");
                })
                .body(ProcessResponse.class);

        String resultText = bResponse != null ? bResponse.result() : "";

        ProcessingLog processingLog = new ProcessingLog();
        processingLog.setUserId(user.getId());
        processingLog.setInputText(request.text());
        processingLog.setOutputText(resultText);

        logRepository.save(processingLog);

        return new ProcessResponse(resultText);
    }
}