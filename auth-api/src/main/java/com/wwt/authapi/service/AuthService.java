package com.wwt.authapi.service;

import com.wwt.authapi.dto.LoginRequest;
import com.wwt.authapi.dto.RegisterRequest;
import jakarta.validation.Valid;

public interface AuthService {

    void register(@Valid RegisterRequest request);

    String authenticate(@Valid LoginRequest request);
}
