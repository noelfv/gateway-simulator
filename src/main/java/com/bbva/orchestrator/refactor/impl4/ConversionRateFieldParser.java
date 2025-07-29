package com.bbva.orchestrator.refactor.impl4;

import com.bbva.orchestrator.refactor.impl4.commons.IFieldDefinition;
import com.bbva.orchestrator.refactor.impl4.commons.ISOUtil;
import com.bbva.orchestrator.refactor.impl4.commons.ParsedFieldResult;
import com.bbva.orchlib.parser.ParserException;

public class ConversionRateFieldParser implements FieldParserStrategy {
    @Override
    public ParsedFieldResult parse(String rawDataSegment, int expectedHexLength, IFieldDefinition fieldDefinition) {
        if (rawDataSegment.length() < expectedHexLength) {
            throw new ParserException("Trama demasiado corta para el campo de tasa de conversión " + fieldDefinition.getIdentifier() + ". Esperado: " + expectedHexLength + ", Disponible: " + rawDataSegment.length());
        }

        String extractedHex = rawDataSegment.substring(0, expectedHexLength);

        String firstByteHex = extractedHex.substring(0, 2);
        String firstDigitStr = ISOUtil.ebcdicToString(firstByteHex);

        String remainingHex = extractedHex.substring(2);
        String numericPartStr = ISOUtil.ebcdicToString(remainingHex);

        int decimalPositions;
        try {
            decimalPositions = Integer.parseInt(firstDigitStr);
        } catch (NumberFormatException e) {
            throw new ParserException("Primer dígito inválido para posiciones decimales en campo " + fieldDefinition.getIdentifier() + ": " + firstDigitStr, e);
        }

        String formattedValue;
        if (numericPartStr.length() < decimalPositions) {
            formattedValue = String.format("%" + decimalPositions + "s", numericPartStr).replace(' ', '0');
            formattedValue = "0." + formattedValue;
        } else {
            int insertIndex = numericPartStr.length() - decimalPositions;
            formattedValue = numericPartStr.substring(0, insertIndex) + "." + numericPartStr.substring(insertIndex);
            if (formattedValue.startsWith(".")) {
                formattedValue = "0" + formattedValue;
            }
        }

        return new ParsedFieldResult(formattedValue, expectedHexLength);
    }

    @Override
    public String build(String fieldValue, IFieldDefinition fieldDefinition) {
        String[] parts = fieldValue.split("\\.");
        String integerPart = parts[0];
        String decimalPart = (parts.length > 1) ? parts[1] : "";

        int decimalPositions = decimalPart.length();

        String numericValueWithoutDecimal = integerPart.replaceFirst("^0+(?!$)", "") + decimalPart;

        int expectedNumericPartLength = fieldDefinition.getLength() - 1;
        String paddedNumericPart = ISOUtil.padAsciiLeft(numericValueWithoutDecimal, expectedNumericPartLength);

        String finalAsciiValue = String.valueOf(decimalPositions) + paddedNumericPart;

        return ISOUtil.stringToEBCDICHex(finalAsciiValue);
    }
}