package com.bbva.orchestrator.parser.refactor.parser.iso8583.strategy.impl;

import com.bbva.orchestrator.parser.refactor.parser.iso8583.strategy.FieldParserStrategy;
import com.bbva.orchestrator.parser.refactor.parser.iso8583.definitions.IFieldDefinition;
import com.bbva.orchestrator.parser.refactor.parser.iso8583.ParsedFieldResult;
import com.bbva.orchestrator.parser.refactor.utils.ISOUtil;
import com.bbva.orchlib.parser.ParserException;

/**
 * Implementación de la estrategia de parsing para campos BinaryString.
 * Esta clase maneja la conversión de datos BinaryString a partir de segmentos hexadecimales
 * y viceversa.
 * */
public class BinaryStringFieldParser implements FieldParserStrategy {
    @Override
    public ParsedFieldResult parse(String rawDataSegment, int expectedHexLength, IFieldDefinition fieldDefinition) {
        if (rawDataSegment.length() < expectedHexLength) {
            throw new ParserException("Trama demasiado corta para el campo binario " + fieldDefinition.getIdentifier() + ". Esperado: " + expectedHexLength + ", Disponible: " + rawDataSegment.length());
        }
        String extractedHex = rawDataSegment.substring(0, expectedHexLength);
        String decodedValue = ISOUtil.convertHEXtoBITMAP(extractedHex);
        return new ParsedFieldResult(decodedValue, expectedHexLength);
    }

    @Override
    public String build(String fieldValue, IFieldDefinition fieldDefinition) {
        return ISOUtil.convertBITMAPtoHEX(fieldValue);
    }
}
