package com.bbva.orchestrator.core.parser.iso8583.strategy.fields;

import com.bbva.orchestrator.core.exception.ParserLocalException;
import com.bbva.orchestrator.core.fields.definitions.IFieldDefinition;
import com.bbva.orchestrator.core.parser.iso8583.ParsedFieldResult;
import com.bbva.orchestrator.core.parser.iso8583.handlers.NetworkHandlerField;
import com.bbva.orchestrator.core.parser.iso8583.strategy.FieldParserStrategy;
import com.bbva.orchestrator.core.utils.ISOUtil;

public class NumericDecimalFieldParser implements FieldParserStrategy {

    @Override
    public ParsedFieldResult parse(String rawDataSegment, IFieldDefinition fieldDefinition, NetworkHandlerField networkHandlerField) {
        try{
            int expectedHexLengthCalculated = networkHandlerField.decodeLengthField(fieldDefinition);
            String extractedHex = rawDataSegment.substring(0, expectedHexLengthCalculated);
            String decodedValue = networkHandlerField.decode(extractedHex,fieldDefinition.getTypeData());
            String decodedDecimalValue = ISOUtil.validAmount(decodedValue);
            return new ParsedFieldResult(decodedDecimalValue, expectedHexLengthCalculated);
        } catch (RuntimeException e) {
            throw new ParserLocalException("PGWP-00103","Error procesando campo " + fieldDefinition.getIdentifier() + " ¨[" + rawDataSegment + "]",e);
        }
    }

    @Override
    public String build(String processedDataSegment, IFieldDefinition fieldDefinition, NetworkHandlerField networkHandlerField) {
        String cleanValue = ISOUtil.revertValidAmount(processedDataSegment);
        return networkHandlerField.encode(cleanValue,fieldDefinition.getTypeData());
    }

}