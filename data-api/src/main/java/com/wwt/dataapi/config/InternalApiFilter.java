package com.wwt.dataapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wwt.dataapi.dto.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1)
@RequiredArgsConstructor
public class InternalApiFilter extends OncePerRequestFilter {

    private static final String INTERNAL_TOKEN_HEADER = "X-Internal-Token";

    @Value("${INTERNAL_TOKEN:}")
    private String validInternalToken;

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String providedToken = request.getHeader(INTERNAL_TOKEN_HEADER);

        if (validInternalToken == null || validInternalToken.isBlank()) {
            sendForbiddenError(response, request, "Service not properly configured");
            return;
        }

        if (!validInternalToken.equals(providedToken)) {
            sendForbiddenError(response, request, "Unauthorized internal request");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendForbiddenError(HttpServletResponse response, HttpServletRequest request, String message) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpServletResponse.SC_FORBIDDEN,
                message,
                request.getRequestURI()
        );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}