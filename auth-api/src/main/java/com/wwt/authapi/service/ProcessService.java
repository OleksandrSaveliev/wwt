package com.wwt.authapi.service;

import com.wwt.authapi.dto.ProcessRequest;
import com.wwt.authapi.dto.ProcessResponse;

public interface ProcessService {
    ProcessResponse processRequest(ProcessRequest request, String email);
}
