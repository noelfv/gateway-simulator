package com.bbva.orchestrator.core.logic;

import com.bbva.orchestrator.core.builders.ISO8583;

import java.util.Map;

public interface NetworkFieldLogic {

    ISO8583 buildISO8583(String originalMessage, Map<String, String> mapValues); // Tipo cambiado a IFieldDefinition


}
