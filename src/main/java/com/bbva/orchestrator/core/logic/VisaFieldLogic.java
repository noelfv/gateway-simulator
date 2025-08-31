package com.bbva.orchestrator.core.logic;

import com.bbva.orchestrator.core.builders.ISO8583;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VisaFieldLogic implements NetworkFieldLogic {

    @Override
    public ISO8583 buildISO8583(String originalMessage, Map<String, String> mapValues) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
