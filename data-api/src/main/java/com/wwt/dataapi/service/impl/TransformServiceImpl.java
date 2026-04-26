package com.wwt.dataapi.service.impl;

import com.wwt.dataapi.service.TransformService;
import org.springframework.stereotype.Service;

@Service
public class TransformServiceImpl implements TransformService {

    @Override
    public String transform(String input) {
        return new StringBuilder(input)
                .reverse()
                .toString()
                .toUpperCase();
    }
}
