package com.example.exception;

import java.util.HashMap;
import java.util.Map;

public class GlobalException extends RuntimeException {

    private final Map<String, String> errors = new HashMap<>();

    public GlobalException(Map<String, String> errors) {
        this.errors.putAll(errors);
    }
    public Map<String, String> getErrors() {
        return errors;
    }
}