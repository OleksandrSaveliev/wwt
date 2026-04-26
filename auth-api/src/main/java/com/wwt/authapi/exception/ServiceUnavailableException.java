package com.wwt.authapi.exception;

// ── 502 Bad Gateway ───────────────────────────────────────────────────────────
public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String serviceName) {
        super("Upstream service is unavailable: " + serviceName);
    }

    public ServiceUnavailableException(String serviceName, Throwable cause) {
        super("Upstream service is unavailable: " + serviceName, cause);
    }
}
