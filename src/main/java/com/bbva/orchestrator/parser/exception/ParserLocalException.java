package com.bbva.orchestrator.parser.exception;

import com.bbva.gateway.sensitivedata.SensitiveDataHandler;
import com.bbva.orchlib.parser.ParserException;
import java.io.Serial;
import java.util.Map;

public class ParserLocalException extends ParserException {

    @Serial
    private static final long serialVersionUID = -3264483332719200704L;
    private Map<String, String> valuesMap;

    public ParserLocalException(String message) {
        super(message);
    }

    public ParserLocalException(String message, Map<String, String> valuesMap) {
        super(message);
        this.valuesMap = valuesMap;
    }

    public Map<String, String> getValuesMap() {
        Map<String, String> valuesMapPSI=valuesMap;
       // valuesMapPSI.remove("primaryAccountNumber");
        //valuesMapPSI.remove("primaryAccountNumber");
        //valuesMapPSI.remove("primaryAccountNumber");
        valuesMapPSI.computeIfPresent("primaryAccountNumber", (key, value) -> SensitiveDataHandler.obfuscate(value, "left", "*", 6, 6));
        return valuesMapPSI;
    }
}
