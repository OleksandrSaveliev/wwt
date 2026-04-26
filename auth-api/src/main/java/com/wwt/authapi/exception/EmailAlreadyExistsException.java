package com.wwt.authapi.exception;

// ── 409 Conflict ─────────────────────────────────────────────────────────────
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Email is already in use: " + email);
    }
}
