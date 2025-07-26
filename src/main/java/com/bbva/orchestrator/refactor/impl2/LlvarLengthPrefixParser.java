package com.bbva.orchestrator.refactor.impl2;

import com.bbva.orchestrator.parser.common.ISODataType;
import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchlib.parser.ParserException;

public class LlvarLengthPrefixParser implements FieldParserStrategy {
    private final FieldParserStrategy actualValueParser; // La estrategia para el valor real del campo

    public LlvarLengthPrefixParser(FieldParserStrategy actualValueParser) {
        this.actualValueParser = actualValueParser;
    }

    @Override
    public ParsedFieldResult parse(String rawDataSegment, ISOField isoField) {
        // isoField.getLength() para LLVAR/LLLVAR es el número de dígitos en el prefijo de longitud (ej. 2 o 3)
        int prefixDigits = isoField.getLength();
        int prefixLengthInChars; // Longitud real del prefijo en caracteres HEX

        // Determinar la longitud HEX del prefijo basada en si es BCD o EBCDIC
        // Por convención, los prefijos de longitud suelen ser BCD o numéricos EBCDIC.
        // Asumimos que si el isoField.getTypeData() es BINARY_STRING, el prefijo también es BCD.
        if (isoField.getTypeData() == ISODataType.BINARY_STRING) {
            // Si el prefijo es BCD, 2 dígitos = 1 byte = 2 chars HEX. 3 dígitos = 2 bytes (relleno) = 4 chars HEX.
            prefixLengthInChars = (int) Math.ceil(prefixDigits / 2.0) * 2;
        } else {
            // Si el prefijo es EBCDIC numérico, 1 dígito = 1 EBCDIC char = 2 chars HEX.
            prefixLengthInChars = prefixDigits * 2;
        }

        if (rawDataSegment.length() < prefixLengthInChars) {
            throw new ParserException("Trama demasiado corta para leer el prefijo de longitud del campo " + isoField.getId() + ". Esperado: " + prefixLengthInChars + " chars, Disponible: " + rawDataSegment.length());
        }

        String lengthPrefixHex = rawDataSegment.substring(0, prefixLengthInChars);
        int actualValueLengthInBytes; // Longitud del valor real del campo en bytes/caracteres decodificados

        // Parsear el valor del prefijo de longitud
        if (isoField.getTypeData() == ISODataType.BINARY_STRING) {
            actualValueLengthInBytes = Integer.parseInt(ISOUtil.hexToDecimal(lengthPrefixHex));
        } else {
            actualValueLengthInBytes = Integer.parseInt(ISOUtil.ebcdicToString(lengthPrefixHex));
        }

        int actualValueHexLength = actualValueLengthInBytes * 2; // Longitud del valor real del campo en caracteres HEX
        //int actualValueHexLength = actualValueLengthInBytes; // Longitud del valor real del campo en caracteres HEX

        // Verificar si rawDataSegment es lo suficientemente largo para el valor real del campo
        if (rawDataSegment.length() < prefixLengthInChars + actualValueHexLength) {
            throw new ParserException("Trama demasiado corta para el valor real del campo variable " + isoField.getId() + ". Esperado: " + (prefixLengthInChars + actualValueHexLength) + ", Disponible: " + rawDataSegment.length());
        }

        String actualFieldDataHex = rawDataSegment.substring(prefixLengthInChars, prefixLengthInChars + actualValueHexLength);

        // Delegar al parser del valor real para decodificar el valor
        String parsedValue = actualValueParser.parse(actualFieldDataHex, isoField).getValue(); // Obtener solo el valor

        int totalConsumedLength = prefixLengthInChars + actualValueHexLength;
        return new ParsedFieldResult(parsedValue, totalConsumedLength);
    }

    @Override
    public String build(String fieldValue, ISOField isoField) {
        String actualRawValueHex = actualValueParser.build(fieldValue, isoField);
        int actualLengthInBytes = actualRawValueHex.length() / 2;

        String prefixValueString = String.format("%0" + isoField.getLength() + "d", actualLengthInBytes);

        String prefixHex;
        if (isoField.getTypeData() == ISODataType.BINARY_STRING) {
            prefixHex = ISOUtil.decimalToHex(prefixValueString);
            if (prefixHex.length() < (int) Math.ceil(isoField.getLength() / 2.0) * 2) {
                prefixHex = String.format("%0" + ((int) Math.ceil(isoField.getLength() / 2.0) * 2) + "s", prefixHex).replace(' ', '0');
            }
        } else {
            prefixHex = ISOUtil.stringToEbcdicHex(prefixValueString);
        }

        return prefixHex + actualRawValueHex;
    }
}
