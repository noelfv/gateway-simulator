package com.bbva.orchlib.utils;

import java.util.Arrays;
import java.util.List;

import com.bbva.orchlib.enums.LRARuleType;
import com.bbva.orchlib.configuration.preloaddto.businessdatalocal.BusinessDataLocal;
import com.bbva.orchlib.configuration.preloaddto.businessdatalocal.InputLRA;
import com.bbva.orchlib.configuration.preloaddto.businessdatalocal.OutputLRA;

public class BusinessDataLocalUtils {

	private static List<BusinessDataLocal> businessDataLocal;
	
	private BusinessDataLocalUtils() {}
	
	public static void setBusinessDataLocalList(List<BusinessDataLocal> bdl) {
		BusinessDataLocalUtils.businessDataLocal = bdl;
    }
	
	/**
     * Obtiene el valor asociado con la clave especificada para el tipo de regla LRARuleType y el tipo de red dados.
     *
     * @param lraRuleType El LRARuleType (INPUT u OUTPUT) para el cual se va a recuperar el valor.
     * @param tipoRed     El tipo de red.
     * @param key         La clave cuyo valor se va a recuperar.
     * @return El valor asociado con la clave para el LRARuleType y el tipo de red especificados, o la clave misma si no se encuentra.
     */
    public static String getLRAValue(LRARuleType lraRuleType, String tipoRed, String key) {
        if (key == null || key.isBlank()) {
            return "";
        }
        return businessDataLocal.stream()
                .filter(dataLocal -> dataLocal.getNetwork().equals(tipoRed))
                .findFirst()
                .map(dataLocal -> {
                    if (lraRuleType == LRARuleType.INPUT) {
                        return dataLocal.getInputLRA().stream()
                                .filter(inputLRA ->
                                        Arrays.stream(inputLRA.getKey().split(","))
                                                .map(String::trim)
                                                .anyMatch(k -> k.equals(key))
                                )
                                .map(InputLRA::getValue)
                                .findFirst()
                                .orElse(key);
                    } else {
                        return dataLocal.getOutputLRA().stream()
                                .filter(outputLRA ->
                                        Arrays.stream(outputLRA.getKey().split(","))
                                                .map(String::trim)
                                                .anyMatch(k -> k.equals(key))
                                )
                                .map(OutputLRA::getValue)
                                .findFirst()
                                .orElse(key);
                    }
                })
                .orElse(key);
    }
	
}
