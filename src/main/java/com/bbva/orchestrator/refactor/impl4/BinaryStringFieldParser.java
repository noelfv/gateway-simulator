package com.bbva.orchestrator.refactor.impl4;

import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchestrator.refactor.impl4.subfields.IFieldDefinition;
import com.bbva.orchlib.parser.ParserException;

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
