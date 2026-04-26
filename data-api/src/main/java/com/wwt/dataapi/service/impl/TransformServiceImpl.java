package com.wwt.dataapi.service.impl;

import com.wwt.dataapi.service.TransformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransformServiceImpl implements TransformService {

    @Override
    public String transform(String input) {
        log.debug("Transforming input of length {}", input.length());

        String result = new StringBuilder(input)
                .reverse()
                .toString()
                .toUpperCase();

        log.debug("Transform complete: '{}' -> '{}'", input, result);

        return result;
    }
}
