package com.bbva.orchestrator.core.parser.iso8583.strategy.fields;

import com.bbva.orchestrator.core.exception.ParserFieldsException;
import com.bbva.orchestrator.core.parser.iso8583.strategy.FieldParserStrategy;
import com.bbva.orchestrator.core.fields.definitions.IFieldDefinition;
import com.bbva.orchestrator.core.parser.iso8583.ParsedFieldResult;
import com.bbva.orchestrator.core.parser.iso8583.handlers.NetworkHandlerField;
import lombok.Getter;

@Getter
public class  LlvarLengthPrefixParser implements FieldParserStrategy {

    private final FieldParserStrategy actualValueParser;

    public LlvarLengthPrefixParser(FieldParserStrategy actualValueParser) {
        this.actualValueParser = actualValueParser;
    }

    @Override
    public ParsedFieldResult parse(String rawDataSegment,  IFieldDefinition fieldDefinition, NetworkHandlerField networkHandlerField) {
        try{
            int prefixLengthInChars = networkHandlerField.getHeaderFieldVar(fieldDefinition);
            int actualValueDecLength= networkHandlerField.decodeHeaderFieldVar(prefixLengthInChars,rawDataSegment, fieldDefinition);
            String actualFieldDataHex = rawDataSegment.substring(prefixLengthInChars, prefixLengthInChars + actualValueDecLength);
            String actualValueResult = networkHandlerField.decode(actualFieldDataHex,fieldDefinition.getTypeData());
            String parsedValue = actualValueResult;
            int totalConsumedLength = prefixLengthInChars + actualValueDecLength;
            return new ParsedFieldResult(parsedValue, totalConsumedLength);
        } catch (ParserFieldsException e) {
            throw e; // Re-throw custom exceptions directly
        }catch (RuntimeException e) {
            throw new ParserFieldsException("PGWP-00106","Error procesando campo " + fieldDefinition.getIdentifier() + " ¨[" + rawDataSegment + "]",e);
        }
    }

    @Override
    public String build(String fieldValue, IFieldDefinition fieldDefinition, NetworkHandlerField networkHandlerField) {
        String actualRawValue = fieldValue;
        String prefixHex = networkHandlerField.encodeHeaderFieldVar(actualRawValue, fieldDefinition);
        String actualRawValueHex = networkHandlerField.encode(actualRawValue, fieldDefinition.getTypeData());
        return prefixHex + actualRawValueHex;
    }

}