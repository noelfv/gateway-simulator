package com.bbva.orchestrator.core.utils;

import com.bbva.gateway.dto.iso20022.AdditionalIdDTO;
import com.bbva.gateway.dto.iso20022.AdditionalInformationDTO;
import com.bbva.gateway.dto.iso20022.ProcessingResultDTO;
import com.bbva.gateway.dto.iso20022.ResultDataDTO;
import com.bbva.orchestrator.configuration.ApplicationDataCache;
import com.bbva.orchestrator.configuration.ApplicationDataLocalCache;
import com.bbva.orchestrator.core.dto.ISO8583;
import com.bbva.orchestrator.core.enums.ResultaDataType;
import com.bbva.orchestrator.core.network.mastercard.MastercardAxisOperator;
import com.bbva.orchestrator.core.network.visa.VisaAxisOperator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class MapperUtil {

    private static final String NETWORK_PEER01 = "PEER01";
    private static final String NETWORK_PEER02 = "PEER02";
    private static final String APPROVED = "Approved";
    private static final String DENIED = "Denied";
    private static final String CODE_00 = "00";
    private static final String RESPONSE_CODE_SECTION  = "response_code";
    private static final List<String> MTI_OUTPUT = List.of("0110", "0130", "0410", "0430","0120", "0420", "0312");
    private final ApplicationDataCache applicationDataCache;
    private final ApplicationDataLocalCache applicationDataLocalCache;

    public ProcessingResultDTO createProcessingResult(ISO8583 inputObject) {

        if (MTI_OUTPUT.contains(inputObject.getMessageType())) {
            String result = ResultaDataType.convertResultDataType(inputObject.getMessageType());
            String otherResult = applicationDataLocalCache.getCustomValue(inputObject.getNetworkName(), "response_code", inputObject.getResponseCode());
            String additionalValue = additionalInfoValue(inputObject.getResponseCode());

            ResultDataDTO resultData = ResultDataDTO.builder()
                    // ======== FIELD 39 (RESPONSE CODE) ========
                    .result(result)
                    .otherResult(otherResult)
                    .otherResultDetails(inputObject.getResponseCode())
                    .build();

            AdditionalInformationDTO additionalInformation = AdditionalInformationDTO.builder()
                    .key("transaction")
                    .value(additionalValue)
                    .build();

            List<AdditionalInformationDTO> additionalInformationList = new ArrayList<>();
            additionalInformationList.add(additionalInformation);

            return ProcessingResultDTO.builder()
                    .resultData(resultData)
                    // ======== FIELD 38 (AUTHORIZATION IDENTIFICATION RESPONSE) ========
                    .approvalCode(inputObject.getAuthorizationIdentificationResponse())
                    .additionalInformation(additionalInformationList)
                    .build();
        }
        return null;
    }

    public static String additionalInfoValue(String responseCode){
        return CODE_00.equals(responseCode) ? APPROVED : DENIED;
    }
    // currencyId=604 -> currencyCode=PEN
    public String convertCurrencyIdToCurrencyCode(String currencyId) {
        return applicationDataCache.getCurrencyCode(currencyId);
    }
    //  currencyCode=PEN -> currencyId=604
    public String convertCurrencyCodeToCurrencyId(String currencyCode) {
        return applicationDataCache.getCurrencyCode(currencyCode);
    }

    public String getBinDescription(String network,String bin) {
        return applicationDataCache.getBinDescription(network,bin);
    }

    public String convertResultDataToResponseCode(String network,String resultData) {
        return applicationDataLocalCache.getCustomValue(network,RESPONSE_CODE_SECTION,resultData);
    }

    public String convertResponseCodeToResultData(String network,String responseCode) {
        return applicationDataLocalCache.getCustomValue(network,RESPONSE_CODE_SECTION,responseCode);
    }

    public String convertEffectiveExchangeRate(String conversionRate) {
        return FieldUtil.convertEffectiveExchangeRate(conversionRate);
    }

    public String convertConversionRate(String effectiveExchangeRate) {
        return FieldUtil.convertConversionRate(effectiveExchangeRate);
    }



    public Double convertAmountDouble(String amount) {
        return FieldUtil.convertAmountDouble(amount);
    }

    /**
     * Convierte un valor double a un String de 12 caracteres.
     * El método elimina el punto decimal y rellena con ceros a la izquierda
     * hasta alcanzar la longitud de 12.
     *
     * @param amount El valor double a convertir.
     * @return Un String de 12 caracteres.
     */
    public String convertAmountString(Double amount) {
        return FieldUtil.convertAmountString(amount);
    }

    public String isNullOrEmptySubstring(String source, int begin, int end) {
        return FieldUtil.isNullOrEmptySubstring(source, begin, end);
    }

    public String convertFormatDateTime(String input) {
        return FieldUtil.convertFormatDateTime(input);
    }

    public String reConvertFormatDateTime(String input) {
        return FieldUtil.reConvertFormatDateTime(input);
    }

    public String convertFormatExpiryDate(String expiry) {
        //return FieldUtil.convertToLastDayOfMonth(expiry);
        return FieldUtil.convertFormatDateExpiration(expiry);
    }

    public String reConvertFormatExpiryDate(String expiry) {
        return FieldUtil.reConvertFormatExpiryDate(expiry);
    }

    public Double conversionRateValidation(String rate) {
        return FieldUtil.conversionRateValidation(rate);
    }


    public String operationTypeValue(String transactionType) {
        if (transactionType == null) {
            return null;
        }

        return switch (transactionType) {
            case "00" -> "PURCHASE";
            case "01" -> "WTHDMON";
            case "20" -> "REFUND";
            default -> null;
        };
    }
    public String generateTransactionReferenceFromSeed(String seed){

        byte[] bytes = seed.getBytes(StandardCharsets.UTF_8);
        byte[] truncate = Arrays.copyOf(bytes, 16);

        UUID uuid= UUID.nameUUIDFromBytes(truncate);

        return uuid.toString().substring(0,35);
    }

    public String validValue(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value;
    }

    public String channelValue(Boolean isEcommerce, String tpvIndicator , String terminalKey) {
        if (isEcommerce != null && isEcommerce) return "ECOMMER";
        if (terminalKey != null && (terminalKey.startsWith("ATM") || "ATMT".equals(tpvIndicator))) return "ATM";
        return "POS";
    }

    public String entryModeValue(ISO8583 iso8583, Map<String, String> subFields, String cardDataEntryMode) {
        if("peer02".equalsIgnoreCase(iso8583.getNetworkName())){
            return MastercardAxisOperator.entryModeIndicator(subFields, cardDataEntryMode);
        }else {
            //return ProcessSubFieldsVisa.entryModeIndicator(subFields, values.getMerchantType());
            return VisaAxisOperator.entryModeIndicator(subFields, cardDataEntryMode);
        }
    }

    public String ownerValue(String channel) {
        return  "RETV".equals(channel) ? "OFFUS" : "ONUS";
    }

    //TODO revisar la nueva estructura de los mensajes
    public  String createTransactionReference(ISO8583 inputObject, Map<String, String> subFields, String networkName){
        StringBuilder transactionReference = new StringBuilder();

        String field11 = isNullOrEmpty(inputObject.getSystemTraceAuditNumber());
        String field32 = isNullOrEmpty(inputObject.getAcquiringInstitutionIdentificationCode());
        String field37 = isNullOrEmpty(inputObject.getRetrievalReferenceNumber());
        String field41 = isNullOrEmpty(inputObject.getCardAcceptorTerminalIdentification());
        String field63 = isNullOrEmpty(inputObject.getNetworkData());
        String field63Part1 = subFields.get("63.01");

        if (networkName.equalsIgnoreCase(NETWORK_PEER01)) {
            transactionReference.append(field11)
                    .append(field32)
                    .append(field37)
                    .append(field41)
                    .append(field63Part1);
        } else if (networkName.equalsIgnoreCase(NETWORK_PEER02)) {
            transactionReference.append(field11)
                    .append(field32)
                    .append(field37)
                    .append(field41)
                    .append(field63);
        }

        return transactionReference.toString();
    }
    private static String isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty() ? "" : value;
    }

    public String channelTPVIndicator(ISO8583 iso8583, Map<String, String> subFields) {
        if("peer02".equalsIgnoreCase(iso8583.getNetworkName())){
            return MastercardAxisOperator.channelTPVIndicator(subFields, iso8583.getMerchantType());
        }else {
            return VisaAxisOperator.channelTPVIndicator(subFields, iso8583.getMerchantType());
        }
    }

    public Boolean channelECommerceIndicator(String networkName, Map<String, String> subFields) {
        if("peer02".equalsIgnoreCase(networkName)){
            return MastercardAxisOperator.channelECommerceIndicator(subFields);
        }else {
            return VisaAxisOperator.channelECommerceIndicator(subFields);
        }
    }

    public String electronicCommerceIndicators(String networkName, Map<String, String> subFields) {
        if("peer02".equalsIgnoreCase(networkName)){
            return MastercardAxisOperator.valueElectronicCommerceIndicators(subFields);
        }else {
            return VisaAxisOperator.valueElectronicCommerceIndicators(subFields);
        }
    }

    public String securityLevelECI(String networkName, String ECI) {
        if("peer02".equalsIgnoreCase(networkName)){
            return MastercardAxisOperator.securityLevelECI(ECI);
        }else {
            return VisaAxisOperator.securityLevelECI(ECI);
        }
    }

    public String defaultIfEmpty(String value, String defaultValue) {
        return StringUtils.defaultIfEmpty(value, defaultValue);
    }

    /**
     * Obtiene un valor de un objeto de origen y maneja de forma segura los valores nulos o vacíos.
     *
     * @param <T>         El tipo del objeto de origen (ej: Card, Acquirer, etc.).
     * @param <R>         El tipo del valor a obtener (ej: String).
     * @param source      El objeto de origen que puede ser nulo.
     * @param getter      Una función para extraer el valor del objeto de origen.
     * @param defaultValue El valor a devolver si el objeto de origen o el valor extraído es nulo o vacío.
     * @return El valor extraído o el valor por defecto.
     */
    public <T, R extends String> R getFieldValue(T source, Function<T, R> getter, R defaultValue) {
        return Optional.ofNullable(source)
                .map(getter)
                .filter(StringUtils::isNotBlank)
                .orElse(defaultValue);
    }

    /**
     * Obtiene el valor de un objeto AdditionalIdDTO si su clave coincide.
     * Si el objeto o la clave no coinciden, devuelve un valor por defecto.
     *
     * @param additionalData El objeto AdditionalIdDTO, que puede ser nulo.
     * @param key La clave esperada.
     * @param defaultValue El valor a devolver si el objeto es nulo, la clave no coincide, o el valor es nulo/vacío.
     * @return El valor asociado a la clave o el valor por defecto.
     */
    public String getAdditionalDataValue(AdditionalIdDTO additionalData, String key, String defaultValue) {
        return Optional.ofNullable(additionalData)
                .filter(data -> key.equals(data.getKey())) // Aplica la condición sobre la clave
                .map(AdditionalIdDTO::getValue)
                .filter(StringUtils::isNotBlank)
                .orElse(defaultValue);
    }

    /**
     * Busca un valor por clave en una lista de AdditionalIdDTO.
     * Si no se encuentra la clave o el valor es nulo/vacío, devuelve el valor por defecto.
     *
     * @param additionalDataList La lista de AdditionalIdDTO.
     * @param key La clave a buscar (ej. "additionalDataRetailer").
     * @param defaultValue El valor por defecto si no se encuentra la clave o el valor.
     * @return El valor asociado a la clave o el valor por defecto.
     */
    public String findValueInAdditionalData(List<AdditionalIdDTO> additionalDataList, String key, String defaultValue) {
        return Optional.ofNullable(additionalDataList)
                .orElse(List.of()) // Si la lista es nula, usamos una lista vacía para evitar NPE.
                .stream()
                .filter(data -> data.getKey() != null && data.getKey().equals(key))
                .map(AdditionalIdDTO::getValue)
                .filter(StringUtils::isNotBlank)
                .findFirst() // Recupera el primer valor que cumpla con la condición
                .orElse(defaultValue); // Si no se encuentra, devuelve el valor por defecto
    }

    // Si necesitas un método que no valide si el String está en blanco, puedes tener otro
    public <T, R> R getObjectValue(T source, Function<T, R> getter, R defaultValue) {
        return Optional.ofNullable(source)
                .map(getter)
                .orElse(defaultValue);
    }

    /**
     * Obtiene un valor de tipo Double de un objeto, lo convierte a String,
     * y devuelve un valor por defecto si el Double es nulo.
     *
     * @param <T>         El tipo del objeto de origen.
     * @param source      El objeto de origen que puede ser nulo.
     * @param getter      Una función para extraer el valor Double del objeto.
     * @param defaultValue El valor String a devolver si el objeto de origen o el Double extraído es nulo.
     * @return El valor Double como String, o el defaultValue.
     */
    public <T> String getFieldValueDouble(T source, Function<T, Double> getter, String defaultValue) {
        return Optional.ofNullable(source)
                .map(getter)
                .map(String::valueOf)
                .orElse(defaultValue);
    }

    public Boolean safeBooleanValueOf(String value) {
        if (value == null) {
            return null;
        }
        return Boolean.valueOf(value);
    }

    public String safeSubstring(String input, int beginIndex, int endIndex) {
        if (input == null || input.isEmpty()) {
            return input; // Retorna el texto original si es nulo o vacío
        }
        if (beginIndex < 0) {
            beginIndex = 0; // Asegura que el índice inicial no sea negativo
        }
        if (endIndex > input.length()) {
            endIndex = input.length(); // Ajusta el índice final si excede la longitud
        }
        if (beginIndex >= endIndex) {
            return ""; // Retorna cadena vacía si los índices no son válidos
        }
        return input.substring(beginIndex, endIndex);
    }

}