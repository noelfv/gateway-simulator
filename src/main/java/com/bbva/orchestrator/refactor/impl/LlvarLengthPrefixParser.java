package com.bbva.orchestrator.refactor.impl;

import com.bbva.orchestrator.parser.common.ISODataType;
import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchestrator.parser.common.ISOUtil;
import com.bbva.orchestrator.refactor.utilsXXXX;

// Para campos de longitud variable (LLVAR, LLLVAR), la estrategia NO parsea el valor,
// sino que extrae la longitud y luego DELEGA a la estrategia del tipo de dato real.
public class LlvarLengthPrefixParser implements FieldParserStrategy {
    private final FieldParserStrategy actualValueParser; // La estrategia para el valor real del campo

    public LlvarLengthPrefixParser(FieldParserStrategy actualValueParser) {
        this.actualValueParser = actualValueParser;
    }

    @Override
    public String parse(String rawData, ISOField isoField) {
        // isoField.getLength() en este caso es la longitud del prefijo (ej. 2 para LLVAR, 3 para LLLVAR)
        int prefixLengthInChars = isoField.getLength(); // Esto es la cantidad de dígitos del prefijo de longitud
        int prefixValue;

        if (isoField.getTypeData() == ISODataType.BINARY_STRING) {
            // Si el prefijo es binario (BCD), la longitud en chars es el doble (ej. 2 dígitos BCD = 1 byte = 2 chars HEX)
            prefixLengthInChars = isoField.getLength() * 2; // Longitud del prefijo en caracteres hexadecimales
            String prefixHex = rawData.substring(0, prefixLengthInChars);
            prefixValue = Integer.parseInt(utilsXXXX.ISOUtil.hexToDecimal(prefixHex)); // Convierte HEX a decimal
        } else {
            // Si el prefijo es ASCII/EBCDIC, la longitud en chars es la misma
            String prefixString = ISOUtil.ebcdicToString(rawData.substring(0, prefixLengthInChars * 2)); // Asume que la trama está en EBCDIC HEX
            prefixValue = Integer.parseInt(prefixString);
        }

        // La data real del campo comienza después del prefijo
        String actualFieldData = rawData.substring(prefixLengthInChars * 2); // Multiplica por 2 si la trama está en HEX

        // Delega al parser del valor real
        return actualValueParser.parse(actualFieldData, isoField);
    }

    @Override
    public String build(String fieldValue, ISOField isoField) {
        // Primero construye el valor real del campo usando la estrategia adecuada
        String actualRawValue = actualValueParser.build(fieldValue, isoField);

        // Luego calcula la longitud real en caracteres (no en bytes) del campo
        int actualLength = actualRawValue.length() / 2; // Suponiendo que actualRawValue es HEX

        // Construye el prefijo de longitud
        String prefixValue = String.format("%0" + isoField.getLength() + "d", actualLength);

        // Si el prefijo debe ser BCD/Binary, conviértelo
        if (isoField.getTypeData() == ISODataType.BINARY_STRING) {
            prefixValue = utilsXXXX.ISOUtil.decimalToHex(prefixValue); // Convierte el número de longitud a HEX
        } else {
            prefixValue = ISOUtil.stringToEBCDICHex(prefixValue); // Convierte el número de longitud a EBCDIC HEX
        }

        return prefixValue + actualRawValue;
    }
}