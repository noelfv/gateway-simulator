package com.bbva.orchestrator.core.fields.definitions.subfields.tlv;

import com.bbva.orchestrator.core.fields.definitions.ISODataType;
import com.bbva.orchestrator.core.fields.definitions.ISOSubField;
import com.bbva.orchestrator.core.parser.iso8583.strategy.FieldParserStrategy;
import com.bbva.orchestrator.core.parser.iso8583.strategy.fields.HexadecimalFieldParser;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Field104 implements ISOSubField {

// ===== DATASETS PRINCIPALES =====

    SF_104_57("57", "BusinessApplicationIdentifier.Dataset57",ISODataType.HEXADECIMAL, true, 2,"","", new HexadecimalFieldParser()),
    SF_104_5F("5F", "SenderRecipientInformation.Dataset5F",ISODataType.HEXADECIMAL, true, 2,"","", new HexadecimalFieldParser()),

    // ===== SUBCAMPOS DE DATASET 1 (01) =====
    SF_104_57_01("104.57.01", "TransacionData.Dataset57",ISODataType.HEXADECIMAL, true, 2,"CardToCardBusinessTransferIndicator","P2PP", new HexadecimalFieldParser()),

    SF_104_5F_02("104.5F.02", "SenderAccountNumber.Dataset5F",ISODataType.HEXADECIMAL, true, 2,"", "", new HexadecimalFieldParser()),
    SF_104_5F_03("104.5F.03", "SenderName.Dataset5F",ISODataType.HEXADECIMAL, true, 2,"", "", new HexadecimalFieldParser()),
    SF_104_5F_05("104.5F.05", "SenderCity.Dataset5F",ISODataType.HEXADECIMAL, true, 2,"", "", new HexadecimalFieldParser()),
    SF_104_5F_07("104.5F.07", "SenderCountryCode.Dataset5F",ISODataType.HEXADECIMAL, true, 2,"", "", new HexadecimalFieldParser()),
    SF_104_5F_08("104.5F.08", "SourceOfFunds.Dataset5F",ISODataType.HEXADECIMAL, true, 2,"", "", new HexadecimalFieldParser()),
    SF_104_5F_0A("104.5F.0A", "RecipientName.Dataset5F",ISODataType.HEXADECIMAL, true, 2, "", "", new HexadecimalFieldParser());

    private final String id;
    private final String name;
    private final ISODataType typeData;
    private final boolean isVariable;
    private final int length;
    private final String type;
    private final String value;
    @Setter
    private FieldParserStrategy parserStrategy;
    @SuppressWarnings("java:S107")
    Field104(String id, String name, ISODataType typeData, boolean isVariable, int length,String type, String value, FieldParserStrategy parserStrategy) {
        this.id = id;
        this.name = name;
        this.typeData = typeData;
        this.isVariable = isVariable;
        this.length = length;
        this.parserStrategy = parserStrategy;
        this.type = type;
        this.value = value;
    }

    private static final Map<String, Field104> lookupMap = new HashMap<>();
    static {
        for (Field104 field : Field104.values()) {
            lookupMap.put(field.getId(), field);
        }
    }

    public static Field104 convertToIso20022(String iso8583Value) {
        return lookupMap.get(iso8583Value);
    }

    @Override
    public String getIdentifier() {
        return this.id;
    }
}
