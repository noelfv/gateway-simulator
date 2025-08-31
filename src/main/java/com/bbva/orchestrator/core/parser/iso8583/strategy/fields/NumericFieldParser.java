package com.bbva.orchestrator.core.parser.iso8583.strategy.fields;

import com.bbva.orchestrator.core.exception.ParserLocalException;
import com.bbva.orchestrator.core.fields.definitions.IFieldDefinition;
import com.bbva.orchestrator.core.parser.iso8583.ParsedFieldResult;
import com.bbva.orchestrator.core.parser.iso8583.handlers.NetworkHandlerField;
import com.bbva.orchestrator.core.parser.iso8583.strategy.FieldParserStrategy;

public class  NumericFieldParser implements FieldParserStrategy {

    @Override
    public ParsedFieldResult parse(String rawDataSegment, IFieldDefinition fieldDefinition, NetworkHandlerField networkHandlerField) {

        try{
            int expectedHexLengthCalculated = networkHandlerField.decodeLengthField(fieldDefinition);
            String extractedHex = rawDataSegment.substring(0, expectedHexLengthCalculated);
            String decodedValue = networkHandlerField.decode(extractedHex,fieldDefinition.getTypeData());
            return new ParsedFieldResult(decodedValue, expectedHexLengthCalculated);
        } catch (RuntimeException e) {
            throw new ParserLocalException("PGWP-00102","Error procesando campo " + fieldDefinition.getIdentifier() + " ¨[" + rawDataSegment + "]",e);
        }
    }

    @Override
    public String build(String fieldValue, IFieldDefinition fieldDefinition, NetworkHandlerField networkHandlerField) {
        return networkHandlerField.encode(fieldValue, fieldDefinition.getTypeData());
    }

}