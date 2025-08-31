package com.bbva.orchestrator.core.exception;

import lombok.Getter;

public class MapperFlowAsynchronousException extends RuntimeException {

    @Getter
    private String code;
    @Getter
    private String description;

    public MapperFlowAsynchronousException(String message) {
        super(message);
    }

    public MapperFlowAsynchronousException(String code, String description, Throwable cause) {
        super(cause);
        this.code=code;
        this.description=description;
    }
}
