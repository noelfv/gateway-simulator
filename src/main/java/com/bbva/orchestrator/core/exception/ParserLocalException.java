package com.bbva.orchestrator.core.exception;

import lombok.Getter;

public class ParserLocalException extends RuntimeException {

    @Getter
    private String code;
    @Getter
    private String description;

    public ParserLocalException(String message) {
        super(message);
    }

    public ParserLocalException(String code,String description,Throwable cause) {
        super(cause);
        this.code=code;
        this.description=description;
    }
}
