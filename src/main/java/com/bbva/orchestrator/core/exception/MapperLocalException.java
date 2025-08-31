package com.bbva.orchestrator.core.exception;

import lombok.Getter;

public class MapperLocalException extends RuntimeException {

    @Getter
    private String code;
    @Getter
    private String description;

    public MapperLocalException(String message) {
        super(message);
    }

    public MapperLocalException(String code, String description, Throwable cause) {
        super(cause);
        this.code=code;
        this.description=description;
    }
}
