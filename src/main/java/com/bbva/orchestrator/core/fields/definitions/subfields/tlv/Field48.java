package com.bbva.orchestrator.core.fields.definitions.subfields.tlv;

import com.bbva.orchestrator.core.fields.definitions.ISODataType;
import com.bbva.orchestrator.core.fields.definitions.ISOSubField;
import com.bbva.orchestrator.core.parser.iso8583.strategy.FieldParserStrategy;
import com.bbva.orchestrator.core.parser.iso8583.strategy.fields.AlphaNumericFieldParser;
import com.bbva.orchestrator.core.parser.iso8583.strategy.fields.HexadecimalFieldParser;
import com.bbva.orchestrator.core.parser.iso8583.strategy.fields.NumericFieldParser;

public enum Field48 implements ISOSubField {

    // Subcampos del Campo 48 (additionalDataRetailer)
    // k: id, v: length in characters

    // Subcampos de longitud fija (longitud en bytes, se multiplica por 2 para chars HEX)
    SF_48_01("01", "AdditionalDataRetailer.01", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()), // Asumiendo que 48.01 es ALPHANUMERIC
    SF_48_05("05", "AdditionalDataRetailer.05", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_09("09", "AdditionalDataRetailer.09", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_10("10", "AdditionalDataRetailer.10", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_11("11", "AdditionalDataRetailer.11", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()), // Inicializado a null
    SF_48_12("12", "AdditionalDataRetailer.12", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()), // Inicializado a null
    SF_48_13("13", "AdditionalDataRetailer.13", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()), // Inicializado a null
    SF_48_14("14", "AdditionalDataRetailer.14", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()), // Inicializado a null
    SF_48_15("15", "AdditionalDataRetailer.15", ISODataType.NUMERIC, false, 1, new NumericFieldParser()), // Inicializado a null
    SF_48_16("16", "AdditionalDataRetailer.16", ISODataType.NUMERIC, false, 1, new NumericFieldParser()), // Inicializado a null
    SF_48_17("17", "AdditionalDataRetailer.17", ISODataType.NUMERIC, false, 1, new NumericFieldParser()), // Inicializado a null
    SF_48_18("18", "AdditionalDataRetailer.18", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_20("20", "AdditionalDataRetailer.20", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_21("21", "AdditionalDataRetailer.21", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_22("22", "AdditionalDataRetailer.22", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_23("23", "AdditionalDataRetailer.23", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_24("24", "AdditionalDataRetailer.24", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_25("25", "AdditionalDataRetailer.25", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_26("26", "AdditionalDataRetailer.26", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_27("27", "AdditionalDataRetailer.27", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_28("28", "AdditionalDataRetailer.28", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_29("29", "AdditionalDataRetailer.29", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_30("30", "AdditionalDataRetailer.30", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_32("32", "AdditionalDataRetailer.32", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_36("36", "AdditionalDataRetailer.36", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_37("37", "AdditionalDataRetailer.37", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_40("40", "AdditionalDataRetailer.40", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_41("41", "AdditionalDataRetailer.41", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_42("42", "AdditionalDataRetailer.42", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_43("43", "AdditionalDataRetailer.43", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_48("48", "AdditionalDataRetailer.48", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_50("50", "AdditionalDataRetailer.50", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_51("51", "AdditionalDataRetailer.51", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_55("55", "AdditionalDataRetailer.55", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_56("56", "AdditionalDataRetailer.56", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_58("58", "AdditionalDataRetailer.58", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_60("60", "AdditionalDataRetailer.60", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_61("61", "AdditionalDataRetailer.61", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_62("62", "AdditionalDataRetailer.62", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_66("66", "AdditionalDataRetailer.66", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_67("67", "AdditionalDataRetailer.67", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_68("68", "AdditionalDataRetailer.68", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_71("71", "AdditionalDataRetailer.71", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_72("72", "AdditionalDataRetailer.72", ISODataType.HEXADECIMAL, false, 1, new HexadecimalFieldParser()),
    SF_48_75("75", "AdditionalDataRetailer.75", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_92("92", "AdditionalDataRetailer.92", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),

    // Subcampos compuestos (con isVariable = true, que tienen sub-subcampos)
    //SF_48_33("33", "AdditionalDataRetailer.33", ISODataType.NUMERIC, true, 0, new CompositeSubFieldParser("48.33")),
    SF_48_33("33", "AdditionalDataRetailer.33", ISODataType.NUMERIC, true, 0, null),

    // Sub-subcampos de Campo 48.33 (internalSubFields48.get("33"))
    SF_48_33_01("33.01", "48.33.01", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_33_02("33.02", "48.33.02", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_33_03("33.03", "48.33.03", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_33_05("33.05", "48.33.05", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_33_06("33.06", "48.33.06", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_33_08("33.08", "48.33.08", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),


    // Subcampos de longitud variable (con isVariable = true, que tienen sub-subcampos)
    SF_48_53("53", "AdditionalDataRetailer.53", ISODataType.NUMERIC, true, 2, null), // Inicializado a null
    //SF_48_56("56", "AdditionalDataRetailer.56", ISODataType.ALPHA_NUMERIC, true, 2, null),// Inicializado a null
    SF_48_57("57", "AdditionalDataRetailer.57", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    // SF_48_61("61", "AdditionalDataRetailer.61", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    SF_48_64("64", "AdditionalDataRetailer.64", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    SF_48_65("65", "AdditionalDataRetailer.65", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    SF_48_74("74", "AdditionalDataRetailer.74", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    SF_48_78("78", "AdditionalDataRetailer.78", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    SF_48_79("79", "AdditionalDataRetailer.79", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    SF_48_93("93", "AdditionalDataRetailer.93", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    //


    // Sub-subcampos de Campo 48.49 (internalSubFields48.get("49"))
    SF_48_49_01("49.01", "TimeValidationInformation.01", ISODataType.NUMERIC, false, 8, new NumericFieldParser()),
    SF_48_49_02("49.02", "TimeValidationInformation.02", ISODataType.NUMERIC, false, 5, new NumericFieldParser()),
    SF_48_49_03("49.03", "TimeValidationInformation.03", ISODataType.NUMERIC, false, 2, new NumericFieldParser()),

    // Sub-subcampos de Campo 48.51 (internalSubFields48.get("51"))
    SF_48_51_01("51.01", "MerchantOnBehalfServices.01", ISODataType.NUMERIC, false, 2, new NumericFieldParser()),
    SF_48_51_02("51.02", "MerchantOnBehalfServices.02", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_51_03("51.03", "MerchantOnBehalfServices.03", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),

    // Sub-subcampos de Campo 48.53 (internalSubFields48.get("53"))
    SF_48_53_01("53.01", "EIDRequestCode.01", ISODataType.NUMERIC, false, 2, new NumericFieldParser()),

    // Sub-subcampos de Campo 48.56 (internalSubFields48.get("56"))
    SF_48_56_01("56.01", "48.56.01", ISODataType.ALPHA_NUMERIC, false, 6, new AlphaNumericFieldParser()),
    SF_48_56_02("56.02", "48.56.02", ISODataType.ALPHA_NUMERIC, false, 6, new AlphaNumericFieldParser()),
    SF_48_56_03("56.03", "48.56.03", ISODataType.ALPHA_NUMERIC, false, 6, new AlphaNumericFieldParser()),

    // Sub-subcampos de Campo 48.57 (internalSubFields48.get("57"))
    SF_48_57_01("57.01", "Subfield57.01", ISODataType.NUMERIC, false, 3, new NumericFieldParser()),
    SF_48_57_02("57.02", "Subfield57.02", ISODataType.NUMERIC, false, 3, new NumericFieldParser()),

    // Sub-subcampos de Campo 48.58 (internalSubFields48.get("58"))
    SF_48_58_01("58.01", "Subfield58.01", ISODataType.NUMERIC, false, 4, new NumericFieldParser()),
    SF_48_58_02("58.02", "Subfield58.02", ISODataType.NUMERIC, false, 6, new NumericFieldParser()),
    SF_48_58_03("58.03", "Subfield58.03", ISODataType.NUMERIC, false, 12, new NumericFieldParser()),
    SF_48_58_04("58.04", "Subfield58.04", ISODataType.NUMERIC, false, 2, new NumericFieldParser()),
    SF_48_58_05("58.05", "Subfield58.05", ISODataType.NUMERIC, false, 2, new NumericFieldParser()),
    SF_48_58_06("58.06", "Subfield58.06", ISODataType.NUMERIC, false, 2, new NumericFieldParser()),
    SF_48_58_07("58.07", "Subfield58.07", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_58_08("58.08", "Subfield58.08", ISODataType.NUMERIC, false, 4, new NumericFieldParser()),

    // Sub-subcampos de Campo 48.64 (internalSubFields48.get("64"))
    SF_48_64_01("64.01", "Subfield64.01", ISODataType.NUMERIC, false, 2, new NumericFieldParser()),
    SF_48_64_02("64.02", "Subfield64.02", ISODataType.NUMERIC, false, 2, new NumericFieldParser()),

    // Sub-subcampos de Campo 48.65 (internalSubFields48.get("65"))
    SF_48_65_01("65.01", "Subfield65.01", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_65_02("65.02", "Subfield65.02", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),

    // Sub-subcampos de Campo 48.71 (internalSubFields48.get("71"))
    SF_48_71_01("71.01", "Subfield71.01", ISODataType.NUMERIC, false, 2, new NumericFieldParser()),
    SF_48_71_02("71.02", "Subfield71.02", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_71_03("71.03", "Subfield71.03", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),

    // Sub-subcampos de Campo 48.74 (internalSubFields48.get("74"))
    SF_48_74_01("74.01", "Subfield74.01", ISODataType.NUMERIC, false, 2, new NumericFieldParser()),
    SF_48_74_02("74.02", "Subfield74.02", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),

    // Sub-subcampos de Campo 48.78 (internalSubFields48.get("78"))
    SF_48_78_01("78.01", "Subfield78.01", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_78_02("78.02", "Subfield78.02", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_78_03("78.03", "Subfield78.03", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_78_04("78.04", "Subfield78.04", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_78_05("78.05", "Subfield78.05", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_78_06("78.06", "Subfield78.06", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),

    // Sub-subcampos de Campo 48.79 (internalSubFields48.get("79"))
    SF_48_79_01("79.01", "Subfield79.01", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_79_02("79.02", "Subfield79.02", ISODataType.NUMERIC, false, 2, new NumericFieldParser()),
    SF_48_79_03("79.03", "Subfield79.03", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_79_04("79.04", "Subfield79.04", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),

    // Sub-subcampos de Campo 48.93 (internalSubFields48.get("93"))
    SF_48_93_01("93.01", "Subfield93.01", ISODataType.NUMERIC, false, 2, new NumericFieldParser()),
    SF_48_93_02("93.02", "Subfield93.02", ISODataType.NUMERIC, false, 17, new NumericFieldParser()),

    ; // <-- Fin de las constantes del enum

    private final String id;
    private final String name;
    private final ISODataType typeData;
    private final boolean isVariable;
    private final int length;
    // CORRECCIÓN: parserStrategy ya no es final
    private FieldParserStrategy parserStrategy;


    Field48(String id, String name, ISODataType typeData, boolean isVariable, int length, FieldParserStrategy parserStrategy) {
        this.id = id;
        this.name = name;
        this.typeData = typeData;
        this.isVariable = isVariable;
        this.length = length;
        this.parserStrategy = parserStrategy;
    }

    @Override
    public String getId() { return id; }
    @Override
    public String getName() { return name; }
    @Override
    public ISODataType getTypeData() { return typeData; }
    @Override
    public boolean isVariable() { return isVariable; }
    @Override
    public int getLength() { return length; }
    @Override
    public FieldParserStrategy getParserStrategy() { return parserStrategy; }

    // Implementación de getIdentifier() para IFieldDefinition
    @Override
    public String getIdentifier() {
        return this.id;
    }

    // Mapas estáticos para búsqueda eficiente por ID
    // CORRECCIÓN: Estos mapas ahora se inicializan y gestionan en ISOMastercardFieldDefinitions
    // private static final Map<String, ISOMastercardSubField48> BY_ID = new HashMap<>();
    // private static final Map<String, Map<String, ISOMastercardSubField48>> SUB_SUBFIELD_MAP = new LinkedHashMap<>();

    // CORRECCIÓN: Eliminar el bloque static de inicialización de mapas aquí
    // La inicialización de los parsers compuestos se hará en ISOMastercardFieldDefinitions
/*
    public static ISOMastercardSubField48 getById(String id) {
        // Delegar a la clase de definiciones
        return ISOMastercardFieldDefinitions.getById(id);
    }

    public static Map<String, ISOMastercardSubField48> getSubSubFieldsForParent(String parentSubFieldId) {
        // Delegar a la clase de definiciones
        return ISOMastercardFieldDefinitions.getSubSubFieldsForParent(parentSubFieldId);
    }

    public static Map<String, ISOMastercardSubField48> getDirectSubFieldDefinitionsForField48() {
        // Delegar a la clase de definiciones
        return ISOMastercardFieldDefinitions.getDirectSubFieldDefinitionsForField48();
    }
*/
    // CORRECCIÓN: Nuevo método para establecer el parserStrategy después de la inicialización
    // Este método será llamado por ISOMastercardFieldDefinitions
    public void setParserStrategy(FieldParserStrategy parserStrategy) {
        this.parserStrategy = parserStrategy;
    }
}