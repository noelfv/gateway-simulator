package com.bbva.orchestrator;

public class ParserException extends RuntimeException{

    private static final long serialVersionUID = -3264483332719200704L;

    public ParserException(String message) {
        super(message);
    }
    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
