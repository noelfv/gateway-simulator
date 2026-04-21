package com.bbva.orchlib.parser;

import java.io.Serial;

public class ParserException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3264483332719200704L;
    
    /**
     * @deprecated And will be DELETED in next updates.
     * You must use InternalServerException as alternative exception class
     */
    @Deprecated(since="2.16.0",forRemoval=true)
    public ParserException(String message) {
        super(message);
    }
}
