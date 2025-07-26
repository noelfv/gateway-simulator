package com.bbva.orchlib.parser;

import java.io.Serial;

public class ParserException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3264483332719200704L;

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message,Throwable throwable) {
        super(message,
                throwable);
    }

}