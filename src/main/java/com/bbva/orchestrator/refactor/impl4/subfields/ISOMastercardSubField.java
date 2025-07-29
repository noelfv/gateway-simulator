package com.bbva.orchestrator.refactor.impl4.subfields;

import com.bbva.orchestrator.parser.common.ISODataType;
import com.bbva.orchestrator.refactor.impl4.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public  enum ISOMastercardSubField implements ISOSubField {

    // Subcampos del Campo 48 (additionalDataRetailerSubFields)
    // k: id, v: length in characters (del SubfieldsUtil original)

    // Subcampos de longitud fija (longitud en bytes, se multiplica por 2 para chars HEX)
    SF_48_01("01", "AdditionalDataRetailer.01", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()), // Asumiendo que 48.01 es ALPHANUMERIC
    SF_48_03("03", "AdditionalDataRetailer.03", ISODataType.NUMERIC, false, 1, new NumericFieldParser()), // AÑADIDO: Definición para subcampo 48.03
    SF_48_05("05", "AdditionalDataRetailer.05", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_09("09", "AdditionalDataRetailer.09", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_18("18", "AdditionalDataRetailer.18", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_21("21", "AdditionalDataRetailer.21", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_22("22", "AdditionalDataRetailer.22", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_24("24", "AdditionalDataRetailer.24", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_25("25", "AdditionalDataRetailer.25", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_27("27", "AdditionalDataRetailer.27", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_32("32", "AdditionalDataRetailer.32", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_33("33", "AdditionalDataRetailer.33", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
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
    SF_48_60("60", "AdditionalDataRetailer.60", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_62("62", "AdditionalDataRetailer.62", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_66("66", "AdditionalDataRetailer.66", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_67("67", "AdditionalDataRetailer.67", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_68("68", "AdditionalDataRetailer.68", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_71("71", "AdditionalDataRetailer.71", ISODataType.ALPHA_NUMERIC, false, 1, new AlphaNumericFieldParser()),
    SF_48_72("72", "AdditionalDataRetailer.72", ISODataType.HEXADECIMAL, false, 1, new HexadecimalFieldParser()),
    SF_48_75("75", "AdditionalDataRetailer.75", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),

    // Subcampos compuestos (con -1 en SubfieldsUtil, que tienen sub-subcampos)
    SF_48_11("11", "AdditionalDataRetailer.11", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    SF_48_13("13", "AdditionalDataRetailer.13", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    SF_48_15("15", "AdditionalDataRetailer.15", ISODataType.NUMERIC, true, 2, null), // Inicializado a null
    SF_48_23("23", "AdditionalDataRetailer.23", ISODataType.NUMERIC, true, 2, null), // Inicializado a null
    SF_48_26("26", "AdditionalDataRetailer.26", ISODataType.NUMERIC, true, 2, null), // Inicializado a null
    SF_48_34("34", "AdditionalDataRetailer.34", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    SF_48_49("49", "AdditionalDataRetailer.49", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
   // SF_48_51("51", "AdditionalDataRetailer.51", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    SF_48_53("53", "AdditionalDataRetailer.53", ISODataType.NUMERIC, true, 2, null), // Inicializado a null
    SF_48_56("56", "AdditionalDataRetailer.56", ISODataType.ALPHA_NUMERIC, true, 2, null),
   // SF_48_56("56", "AdditionalDataRetailer.56", ISODataType.ALPHA_NUMERIC, true, 2, new AlphaNumericFieldParser()),
    //SF_48_56("56", "AdditionalDataRetailer.56", ISODataType.ALPHA_NUMERIC, true, 2, new CompositeSubFieldParser(ISOMastercardSubFieldDefinitions.getSubSubFieldsForParent("56"))),// Inicializado a null
    SF_48_57("57", "AdditionalDataRetailer.57", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    SF_48_58("58", "AdditionalDataRetailer.58", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    SF_48_61("61", "AdditionalDataRetailer.61", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    SF_48_64("64", "AdditionalDataRetailer.64", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    SF_48_65("65", "AdditionalDataRetailer.65", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
   // SF_48_71("71", "AdditionalDataRetailer.71", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    SF_48_74("74", "AdditionalDataRetailer.74", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    SF_48_78("78", "AdditionalDataRetailer.78", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    SF_48_79("79", "AdditionalDataRetailer.79", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null
    SF_48_93("93", "AdditionalDataRetailer.93", ISODataType.ALPHA_NUMERIC, true, 2, null), // Inicializado a null

    // Sub-subcampos de Campo 48.11 (internalSubFields48.get("11"))
    SF_48_11_01("11.01", "48.11.01", ISODataType.NUMERIC, false, 2, new NumericFieldParser()),
    SF_48_11_02("11.02", "48.11.02", ISODataType.NUMERIC, false, 2, new NumericFieldParser()),
    SF_48_11_03("11.03", "48.11.03", ISODataType.NUMERIC, false, 2, new NumericFieldParser()),
    SF_48_11_04("11.04", "48.11.04", ISODataType.ALPHA_NUMERIC, false, 32, new AlphaNumericFieldParser()),
    SF_48_11_05("11.05", "48.11.05", ISODataType.ALPHA_NUMERIC, false, 16, new AlphaNumericFieldParser()),

    // Sub-subcampos de Campo 48.13 (internalSubFields48.get("13"))
    SF_48_13_01("13.01", "MastercardHostedMobilePhoneTopUpRequestData.01", ISODataType.ALPHA_NUMERIC, false, 17, new AlphaNumericFieldParser()),
    SF_48_13_02("13.02", "MastercardHostedMobilePhoneTopUpRequestData.02", ISODataType.ALPHA_NUMERIC, false, 30, new AlphaNumericFieldParser()),

    // Sub-subcampos de Campo 48.15 (internalSubFields48.get("15"))
    SF_48_15_01("15.01", "AuthorizationSystemAdviceDateAndTime.01", ISODataType.NUMERIC, false, 4, new NumericFieldParser()),
    SF_48_15_02("15.02", "AuthorizationSystemAdviceDateAndTime.02", ISODataType.NUMERIC, false, 6, new NumericFieldParser()),

    // Sub-subcampos de Campo 48.23 (internalSubFields48.get("23"))
    SF_48_23_01("23.01", "PaymentInitiationChannel.01", ISODataType.NUMERIC, false, 2, new NumericFieldParser()),

    // Sub-subcampos de Campo 48.26 (internalSubFields48.get("26"))
    SF_48_26_01("26.01", "WalletProgramData.01", ISODataType.NUMERIC, false, 3, new NumericFieldParser()),

    // Sub-subcampos de Campo 48.34 (internalSubFields48.get("34"))
    SF_48_34_01("34.01", "ATCInformation.01", ISODataType.NUMERIC, false, 5, new NumericFieldParser()),
    SF_48_34_02("34.02", "ATCInformation.02", ISODataType.NUMERIC, false, 5, new NumericFieldParser()),
    SF_48_34_03("34.03", "ATCInformation.03", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),

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
    SF_48_56_01("56.01", "48.56.01", ISODataType.NUMERIC, false, 3, new NumericFieldParser()),
    SF_48_56_02("56.02", "48.56.02", ISODataType.NUMERIC, false, 3, new NumericFieldParser()),

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

    // Sub-subcampos de Campo 48.61 (internalSubFields48.get("61"))
    SF_48_61_01("61.01", "48.61.01", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_61_02("61.02", "48.61.02", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    SF_48_61_03("61.03", "48.61.03", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),

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

    ISOMastercardSubField(String id, String name, ISODataType typeData, boolean isVariable, int length, FieldParserStrategy parserStrategy) {
        this.id = id;
        this.name = name;
        this.typeData = typeData;
        this.isVariable = isVariable;
        this.length = length;
        // CORRECCIÓN: Asignar directamente el parser para campos no compuestos
        // Para campos compuestos, se asignará en el bloque estático
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
    private static final Map<String, ISOMastercardSubField> BY_ID = new HashMap<>();
    private static final Map<String, Map<String, ISOMastercardSubField>> SUB_SUBFIELD_MAP = new LinkedHashMap<>();

    // CORRECCIÓN: Eliminar el bloque static de inicialización de mapas aquí
    // La inicialización de los parsers compuestos se hará en ISOMastercardFieldDefinitions

    public static ISOMastercardSubField getById(String id) {
        // Delegar a la clase de definiciones
        return ISOMastercardFieldDefinitions.getById(id);
    }

    public static Map<String, ISOMastercardSubField> getSubSubFieldsForParent(String parentSubFieldId) {
        // Delegar a la clase de definiciones
        return ISOMastercardFieldDefinitions.getSubSubFieldsForParent(parentSubFieldId);
    }

    public static Map<String, ISOMastercardSubField> getDirectSubFieldDefinitionsForField48() {
        // Delegar a la clase de definiciones
        return ISOMastercardFieldDefinitions.getDirectSubFieldDefinitionsForField48();
    }

    // CORRECCIÓN: Nuevo método para establecer el parserStrategy después de la inicialización
    // Este método será llamado por ISOMastercardFieldDefinitions
    public void setParserStrategy(FieldParserStrategy parserStrategy) {
        this.parserStrategy = parserStrategy;
    }
}