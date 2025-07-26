package com.bbva.orchestrator.refactor.impl;

import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchestrator.parser.common.ISOUtil;

// Implementación para NUMERIC_DECIMAL (montos, requiere validación y formato)
public class NumericDecimalFieldParser implements FieldParserStrategy {
    @Override
    public String parse(String rawData, ISOField isoField) {
        String numericValue = ISOUtil.ebcdicToString(rawData);
        // Aquí podrías aplicar la validación de montos o formato específico
        return ISOUtil.validAmount(numericValue); // Suponiendo que validAmount ya maneja ceros y coma/punto decimal
    }

    @Override
    public String build(String fieldValue, ISOField isoField) {
        // Formatear el monto de vuelta a la representación numérica EBCDIC HEX
        // Eliminar puntos/comas, rellenar con ceros
        String cleanValue = fieldValue.replace(".", "").replace(",", ""); // Limpia el monto
        String paddedValue = String.format("%" + isoField.getLength() + "s", cleanValue).replace(' ', '0');
        return ISOUtil.stringToEBCDICHex(paddedValue.substring(0, isoField.getLength()));
    }
}