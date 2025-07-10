package org.example.orchestrator.common;



import org.example.orchestrator.dto.ISO20022;

public class PassThroughValidator {

    private static final String LABEL = "pan";

    private PassThroughValidator() {
    }

    public static boolean isPassThroughEnabled(ISO20022 iso20022) {
        return true;
    }
}
