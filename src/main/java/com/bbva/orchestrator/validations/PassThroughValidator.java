package com.bbva.orchestrator.validations;


import com.bbva.gateway.dto.iso20022.ISO20022;


public class PassThroughValidator {

    private static final String LABEL = "pan";

    private PassThroughValidator() {
    }

    public static boolean isPassThroughEnabled(ISO20022 iso20022) {
        try {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
