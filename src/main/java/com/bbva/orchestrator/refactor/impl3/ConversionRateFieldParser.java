package com.bbva.orchestrator.refactor.impl3;

import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchlib.parser.ParserException;

public class  ConversionRateFieldParser implements FieldParserStrategy {

    @Override
    public ParsedFieldResult parse(String rawDataSegment, int expectedLength, ISOField isoField) {
        if (rawDataSegment.length() < expectedLength) {
            throw new ParserException("Trama demasiado corta para el campo de tasa de conversión " + isoField.getId() + ". Esperado: " + expectedLength + ", Disponible: " + rawDataSegment.length());
        }

        String extractedData = rawDataSegment.substring(0, expectedLength);
        String firstDigitStr;
        String numericPartStr;
        int consumedChars;

            // Extraer el primer byte HEX (2 chars) para el dígito de posiciones decimales
            String firstByteHex = extractedData.substring(0, 2);
            firstDigitStr = ISOUtil.ebcdicToString(firstByteHex); // Decodificar el dígito EBCDIC a ASCII

            // Extraer el resto de la parte numérica (longitud total - 2 chars del primer byte)
            String remainingHex = extractedData.substring(2);
            numericPartStr = ISOUtil.ebcdicToString(remainingHex); // Decodificar el resto EBCDIC a ASCII
            consumedChars = expectedLength; // La longitud consumida es la longitud HEX total


        int decimalPositions;
        try {
            decimalPositions = Integer.parseInt(firstDigitStr);
        } catch (NumberFormatException e) {
            throw new ParserException("Primer dígito inválido para posiciones decimales en campo " + isoField.getId() + ": " + firstDigitStr, e);
        }

        // Insertar el punto decimal
        String formattedValue;
        if (numericPartStr.length() < decimalPositions) {
            // Si la parte numérica es más corta que las posiciones decimales, rellenar con ceros a la izquierda
            formattedValue = String.format("%" + decimalPositions + "s", numericPartStr).replace(' ', '0');
            formattedValue = "0." + formattedValue;
        } else {
            int insertIndex = numericPartStr.length() - decimalPositions;
            formattedValue = numericPartStr.substring(0, insertIndex) + "." + numericPartStr.substring(insertIndex);
            // Si el valor resultante empieza con ".", añadir "0" delante
            if (formattedValue.startsWith(".")) {
                formattedValue = "0" + formattedValue;
            }
        }

        return new ParsedFieldResult(formattedValue, consumedChars);
    }

    @Override
    public String build(String fieldValue, ISOField isoField) {
        // Eliminar el punto decimal y obtener la parte entera y la parte decimal
        String[] parts = fieldValue.split("\\.");
        String integerPart = parts[0];
        String decimalPart = (parts.length > 1) ? parts[1] : "";

        // Calcular las posiciones decimales reales
        int decimalPositions = decimalPart.length();

        // Si el valor es "0.XXX", el "0" inicial no cuenta para la longitud total
        String numericValueWithoutDecimal = integerPart.replaceFirst("^0+(?!$)", "") + decimalPart; // Elimina ceros iniciales si no es solo "0"

        // Asegurarse de que el valor numérico sin decimales tenga la longitud correcta
        // isoField.getLength() es la longitud total del campo (ej. 8), incluyendo el dígito de posiciones decimales.
        // Por lo tanto, el valor numérico real debe ser (isoField.getLength() - 1)
        int expectedNumericPartLength = isoField.getLength() - 1;
        String paddedNumericPart = null;// ISOUtil.padAsciiLeft(numericValueWithoutDecimal, expectedNumericPartLength);

        // Concatenar el dígito de posiciones decimales con la parte numérica
        String finalAsciiValue = String.valueOf(decimalPositions) + paddedNumericPart;

        // Convertir el resultado a HEX EBCDIC
        return ISOUtil.stringToEBCDICHex(finalAsciiValue);
    }
}