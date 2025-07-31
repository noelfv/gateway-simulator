package com.bbva.orchlib.rules;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.gateway.utils.LogsTraces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RulesValidations {

    private RulesValidations() {
    }


    /**
     * Ejecuta una regla de validación específica sobre un objeto de validación dado, utilizando el mapeo ISO 20022 proporcionado.
     *
     * @param rule             La regla de validación a ejecutar.
     * @param objetoValidacion El objeto sobre el que se realizará la validación.
     * @param iso20022map      El mapeo ISO 20022 necesario para la validación.
     * @return true si la validación se ejecuta correctamente y pasa, false de lo contrario.
     */
    public static boolean executeValidationRule(String rule, Object objetoValidacion, ISO20022 iso20022map) {
        try {
            Method metodo = objetoValidacion.getClass().getMethod(rule, ISO20022.class);
            LogsTraces.writeInfo("[RulesValidations]: validación "+ metodo.getName());
            return (boolean) metodo.invoke(objetoValidacion, iso20022map);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException e) {
            LogsTraces.writeError("[RulesValidations]: VALIDATIONS COULD NOT BE EXECUTED: " + e.getMessage());
            return false;
        }
    }
}
