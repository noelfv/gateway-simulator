package com.bbva.orchlib.rules;


import com.bbva.orchlib.enums.LRARuleType;
import com.bbva.orchlib.featuretoggle.businessdatalocal.BusinessDataLocal;
import com.bbva.orchlib.featuretoggle.businessdatalocal.InputLRA;
import com.bbva.orchlib.featuretoggle.businessdatalocal.OutputLRA;

import java.util.List;

public class RulesOrchestrator {


    private static List<BusinessDataLocal> bdl;


    private RulesOrchestrator() {

    }


    public static void setBusinessDataLocalList(List<BusinessDataLocal> bdl) {
        RulesOrchestrator.bdl = bdl;
    }





    /**
     * Método de encontrar Regla recibida, dentro de una lista
     * @param rulesList Lista de reglas
     * @param rule Regla a buscar
     * @return Regresa true, si la regla esta dentro de la lista.
     * 			Regresa false, en caso de no encontrarlo
     */
    public static boolean findRule(List<String> rulesList, String rule) {
        for (String s : rulesList) if (s.equals(rule)) return true;
        return false;
    }



    /**
     * Obtiene el valor asociado con la clave especificada para el tipo de regla LRARuleType y el tipo de red dados.
     *
     * @param lraRuleType El LRARuleType (INPUT u OUTPUT) para el cual se va a recuperar el valor.
     * @param tipoRed     El tipo de red.
     * @param key         La clave cuyo valor se va a recuperar.
     * @return El valor asociado con la clave para el LRARuleType y el tipo de red especificados, o la clave misma si no se encuentra.
     */
    public static String getLRAValue(LRARuleType lraRuleType , String tipoRed, String key) {

        return bdl.stream()
                .filter(dataLocal -> dataLocal.getNetwork().equals(tipoRed))
                .findFirst()
                .map(dataLocal -> {
                    if (lraRuleType == LRARuleType.INPUT) {
                        return dataLocal.getInputLRA().stream()
                                .filter(inputLRA -> inputLRA.getKey().equals(key))
                                .map(InputLRA::getValue)
                                .findFirst()
                                .orElse(key);
                    } else {
                        return dataLocal.getOutputLRA().stream()
                                .filter(outputLRA -> outputLRA.getKey().equals(key))
                                .map(OutputLRA::getValue)
                                .findFirst()
                                .orElse(key);
                    }
                })
                .orElse(key);
    }
}
