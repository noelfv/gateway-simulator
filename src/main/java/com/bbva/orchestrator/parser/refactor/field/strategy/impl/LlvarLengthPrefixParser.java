package com.bbva.orchestrator.parser.refactor.field.strategy.impl;

import com.bbva.orchestrator.parser.refactor.field.strategy.FieldParserStrategy;
import com.bbva.orchestrator.parser.refactor.field.IFieldDefinition;
import com.bbva.orchestrator.parser.refactor.field.ParsedFieldResult;
import com.bbva.orchestrator.parser.refactor.utils.ISOUtil;
import com.bbva.orchlib.parser.ParserException;
import lombok.Getter;

@Getter
public class  LlvarLengthPrefixParser implements FieldParserStrategy {
    // NUEVO MÉTODO: Getter para actualValueParser
    private final FieldParserStrategy actualValueParser;

    public LlvarLengthPrefixParser(FieldParserStrategy actualValueParser) {
        this.actualValueParser = actualValueParser;
    }


    @Override
    public ParsedFieldResult parse(String rawDataSegment, int ignoredExpectedLength, IFieldDefinition fieldDefinition) {
        int prefixDigits = fieldDefinition.getLength();
        int prefixLengthInChars = prefixDigits * 2;

        if (rawDataSegment.length() < prefixLengthInChars) {
            throw new ParserException("Trama demasiado corta para leer el prefijo de longitud del campo " + fieldDefinition.getIdentifier() + ". Esperado: " + prefixLengthInChars + ", Disponible: " + rawDataSegment.length());
        }

        String lengthPrefixHex = rawDataSegment.substring(0, prefixLengthInChars);
        int actualValueLengthInBytes;

        actualValueLengthInBytes = Integer.parseInt(ISOUtil.ebcdicToString(lengthPrefixHex));

        int actualValueHexLength = actualValueLengthInBytes * 2;

        if (rawDataSegment.length() < prefixLengthInChars + actualValueHexLength) {
            throw new ParserException("Trama demasiado corta para el valor real del campo variable " + fieldDefinition.getIdentifier() + ". Esperado: " + (prefixLengthInChars + actualValueHexLength) + ", Disponible: " + rawDataSegment.length());
        }

        String actualFieldDataHex = rawDataSegment.substring(prefixLengthInChars, prefixLengthInChars + actualValueHexLength);

        ParsedFieldResult actualValueResult = actualValueParser.parse(actualFieldDataHex, actualValueHexLength, fieldDefinition);
        String parsedValue = actualValueResult.value();

        int totalConsumedLength = prefixLengthInChars + actualValueHexLength;
        return new ParsedFieldResult(parsedValue, totalConsumedLength);
    }

    @Override
    public String build(String fieldValue, IFieldDefinition fieldDefinition) {
        String actualRawValueHex = actualValueParser.build(fieldValue, fieldDefinition);
        int actualLengthInBytes = actualRawValueHex.length() / 2;

        String prefixValueString = String.format("%0" + fieldDefinition.getLength() + "d", actualLengthInBytes);

        String prefixHex;
        prefixHex = ISOUtil.stringToEBCDICHex(prefixValueString);

        return prefixHex + actualRawValueHex;
    }
}