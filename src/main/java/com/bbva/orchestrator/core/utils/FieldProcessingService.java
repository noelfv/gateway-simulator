package com.bbva.orchestrator.core.utils;

import com.bbva.gateway.dto.iso20022.AdditionalIdDTO;
import com.bbva.orchestrator.core.builders.ISO8583;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Service
public class FieldProcessingService {

    private static final String NETWORK_PEER01 = "PEER01";
    private static final String NETWORK_PEER02 = "PEER02";
    private static final String FIELD63PART1  = "63.1";


    public Double parseDouble(String value) {
        return FieldUtils.parseDouble(value);
    }

    public String isNullOrEmptySubstring(String source, int begin, int end) {
        return FieldUtils.isNullOrEmptySubstring(source, begin, end);
    }



    public String convertFormatDateTime(String input) {
        return FieldUtils.convertFormatDateTime(input);
    }

    public Double conversionRateValidation(String rate) {
        return FieldUtils.conversionRateValidation(rate);
    }

    public String convertFormatExpiryDate(String expiry) {
        return FieldUtils.convertFormatExpiryDate(expiry);
    }

    public  String createTransactionReference(ISO8583 inputObject, Map<String, String> subFields, String networkName){
        StringBuilder transactionReference = new StringBuilder();

        String field11 = isNullOrEmpty(inputObject.getSystemTraceAuditNumber());
        String field32 = isNullOrEmpty(inputObject.getAcquiringInstitutionIdentificationCode());
        String field37 = isNullOrEmpty(inputObject.getRetrievalReferenceNumber());
        String field41 = isNullOrEmpty(inputObject.getCardAcceptorTerminalIdentification());
        String field42 = isNullOrEmpty(inputObject.getCardAcceptorIdentificationCode());
        String field63 = isNullOrEmpty(inputObject.getNetworkData());
        String field63Part1 = subFields.get(FIELD63PART1);

        if (networkName.equalsIgnoreCase(NETWORK_PEER01)) {
            transactionReference.append(field11)
                    .append(field32)
                    .append(field37)
                    .append(field41)
                    .append(field42)
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

    public String channelTPVIndicator(Object input, Map<String, String> subFields, String network) {
        // Placeholder: esta lógica vendría de ISOSubFieldProcess
        return subFields.get("CHANNEL_TPV_INDICATOR");
    }

    public Boolean channelECommerceIndicator(Object input, Map<String, String> subFields, String network) {
        return Objects.equals(subFields.get("E_COMMERCE_INDICATOR"), "true");
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


}