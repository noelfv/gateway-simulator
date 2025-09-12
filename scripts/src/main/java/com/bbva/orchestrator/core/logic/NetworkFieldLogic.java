package com.bbva.orchestrator.core.logic;

import java.util.List;
import java.util.Map;


public interface NetworkFieldLogic {

    List<String> applyLogicFields(String typeMessage, Map<String, String> mapValues, String temp);
    // Tipo cambiado a IFieldDefinition
    List<Integer> applyLogicFields(String typeMessage, Map<Integer, String> mapValues); // Tipo cambiado a IFieldDefinition


}
