package com.bbva.orchestrator.refactor.impl3;

import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchlib.parser.ParserException;

public class VarLengthPrefixParser implements FieldParserStrategy {
    private final FieldParserStrategy actualValueParser; // La estrategia para el valor real del campo

    public VarLengthPrefixParser(FieldParserStrategy actualValueParser) {
        this.actualValueParser = actualValueParser;
    }

    @Override
    public ParsedFieldResult parse(String rawDataSegment, int ignoredExpectedHexLength, ISOField isoField) {
        // isoField.getLength() para LLVAR/LLLVAR es el número de dígitos en el prefijo de longitud (ej. 2 o 3)
        int prefixDigits = isoField.getLength();
        int prefixLengthInChars = prefixDigits * 2; // Longitud real del prefijo en caracteres HEX (ej. 2 dígitos LL = 4 chars HEX)

        if (rawDataSegment.length() < prefixLengthInChars) {
            throw new ParserException("Trama demasiado corta para leer el prefijo de longitud del campo " + isoField.getId() + ". Esperado: " + prefixLengthInChars + " chars, Disponible: " + rawDataSegment.length());
        }

        String lengthPrefixHex = rawDataSegment.substring(0, prefixLengthInChars);
        int actualValueLengthInBytes; // Longitud del valor real del campo en bytes/caracteres decodificados

        // Parsear el valor del prefijo de longitud (asumiendo EBCDIC numérico para LL/LLL)
        // Utiliza el ebcdicToString de tu ISOUtil, que ahora usa Cp1047
        actualValueLengthInBytes = Integer.parseInt(ISOUtil.ebcdicToString(lengthPrefixHex));


        int actualValueHexLength = actualValueLengthInBytes * 2; // Longitud del valor real del campo en caracteres HEX

        // Verificar si rawDataSegment es lo suficientemente largo para el valor real del campo
        if (rawDataSegment.length() < prefixLengthInChars + actualValueHexLength) {
            throw new ParserException("Trama demasiado corta para el valor real del campo variable " + isoField.getId() + ". Esperado: " + (prefixLengthInChars + actualValueHexLength) + ", Disponible: " + rawDataSegment.length());
        }

        String actualFieldDataHex = rawDataSegment.substring(prefixLengthInChars, prefixLengthInChars + actualValueHexLength);

        // Delegar al parser del valor real para decodificar el valor, pasándole la longitud HEX exacta
        ParsedFieldResult actualValueResult = actualValueParser.parse(actualFieldDataHex, actualValueHexLength, isoField);
        String parsedValue = actualValueResult.getValue(); // Obtener solo el valor

        int totalConsumedLength = prefixLengthInChars + actualValueHexLength;
        return new ParsedFieldResult(parsedValue, totalConsumedLength);
    }

    @Override
    public String build(String fieldValue, ISOField isoField) {
        String actualRawValueHex = actualValueParser.build(fieldValue, isoField);
        int actualLengthInBytes = actualRawValueHex.length() / 2; // Longitud del valor real en bytes

        String prefixValueString = String.format("%0" + isoField.getLength() + "d", actualLengthInBytes);

        String prefixHex;
        // La construcción del prefijo también debe ser consistente con su parsing.
        // Si el prefijo es numérico EBCDIC.
        prefixHex = ISOUtil.stringToEBCDICHex(prefixValueString);

        return prefixHex + actualRawValueHex;
    }
}