package com.bbva.orchestrator.core.logic.impl;

import com.bbva.orchestrator.configuration.ApplicationDataLocalCache;
import com.bbva.orchestrator.core.logic.NetworkFieldLogic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MastercardFieldLogic implements NetworkFieldLogic {

    private final ApplicationDataLocalCache applicationDataLocalCache;

    public MastercardFieldLogic(ApplicationDataLocalCache applicationDataLocalCache) {
        this.applicationDataLocalCache = applicationDataLocalCache;
    }

    @Override
    public List<String> applyLogicFields(String typeMessage, Map<String, String> mapValues, String temp) {
        List<String> listMandatoryFields= applicationDataLocalCache.getFieldsMandatory(typeMessage);

        //TODO esta logica debe ir en un servicio aparte
        String field48= mapValues.get("additionalDataRetailer");
        String field48Tag87= mapValues.get("48.87");
        if(field48!=null) {
            if (field48Tag87.equals("true")) {
                mapValues.put("additionalDataRetailer", field48 + "D4");
            }
            if (field48Tag87.equals("false")) {
                mapValues.put("additionalDataRetailer", field48 + "D5");
            }
        }

        //Validar que el mapa contenga los campos mandatorios
        for(String field: listMandatoryFields){
            if(!mapValues.containsKey(field)){
                //trhow runtime exception si no contiene los campos mandatorios
                throw new RuntimeException("El mensaje no contiene el campo mandatorio: "+field);
            }
        }
        List<String> listConditionalFields= applicationDataLocalCache.getFieldsConditional(typeMessage);

        return null;
    }

    @Override
    public List<Integer> applyLogicFields(String typeMessage, Map<Integer, String> mapValues) {


        return null;
    }

}
