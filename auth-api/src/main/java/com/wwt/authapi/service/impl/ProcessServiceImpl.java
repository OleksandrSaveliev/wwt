package com.wwt.authapi.service.impl;

import com.wwt.authapi.dto.*;
import com.wwt.authapi.entity.ProcessingLog;
import com.wwt.authapi.entity.User;
import com.wwt.authapi.exception.ServiceUnavailableException;
import com.wwt.authapi.repository.ProcessingLogRepository;
import com.wwt.authapi.repository.UserRepository;
import com.wwt.authapi.service.ProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {

    private final ProcessingLogRepository logRepository;
    private final UserRepository userRepository;
    private final RestClient restClient;

    private static final String TRANSFORM_PATH = "/api/transform";
    private static final String EXTERNAL_SERVICE_NAME = "Data Transformation Service";

    @Value("${INTERNAL_TOKEN}")
    private String internalToken;

    @Override
    public ProcessResponse processRequest(ProcessRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User context not found: " + email));

        String resultText = callExternalService(request.text());

        saveLog(user, request.text(), resultText);

        log.debug("Processing completed for user: {}", email);
        return new ProcessResponse(resultText);
    }

    private String callExternalService(String inputText) {
        StopWatch sw = new StopWatch();
        sw.start();

        try {
            log.info("Calling external service: {}", EXTERNAL_SERVICE_NAME);

            ProcessResponse response = restClient.post()
                    .uri(TRANSFORM_PATH)
                    .header("X-Internal-Token", internalToken)
                    .body(new ProcessRequest(inputText))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        log.error("External service {} failed with status {}", EXTERNAL_SERVICE_NAME, res.getStatusCode());
                        throw new ServiceUnavailableException(EXTERNAL_SERVICE_NAME);
                    })
                    .body(ProcessResponse.class);

            sw.stop();
            log.info("External service call completed in {}ms", sw.getTotalTimeMillis());

            return Optional.ofNullable(response)
                    .map(ProcessResponse::result)
                    .orElse("");

        } catch (ServiceUnavailableException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error calling {}: {}", EXTERNAL_SERVICE_NAME, e.getMessage());
            throw new ServiceUnavailableException(EXTERNAL_SERVICE_NAME, e);
        }
    }

    private void saveLog(User user, String input, String output) {
        try {
            ProcessingLog processingLog = new ProcessingLog();
            processingLog.setUserId(user.getId());
            processingLog.setInputText(input);
            processingLog.setOutputText(output);

            logRepository.save(processingLog);
            log.debug("Processing log saved for user: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to save processing log for user {}", user.getEmail(), e);
        }
    }
}
