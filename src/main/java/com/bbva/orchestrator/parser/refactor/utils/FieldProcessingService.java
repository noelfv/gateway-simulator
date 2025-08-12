package com.bbva.orchestrator.parser.refactor.utils;


import com.bbva.gateway.dto.iso20022.AdditionalIdDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class FieldProcessingService {

    public Double parseDouble(String value) {
        return FieldUtils.parseDouble(value);
    }


    public BigDecimal conversionRateValidationBig(String value) {
        return FieldUtils.conversionRateValidationBig(value);
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

    public String createTransactionReference(Object inputObject) {
        // Aquí puedes inyectar ProcessMonitoring si es un bean
        return "TRX-" + System.currentTimeMillis(); // placeholder
    }

    public String channelTPVIndicator(Object input, Map<String, String> subFields, String network) {
        // Placeholder: esta lógica vendría de ISOSubFieldProcess
        return subFields.get("CHANNEL_TPV_INDICATOR");
    }

    public Boolean channelECommerceIndicator(Object input, Map<String, String> subFields, String network) {
        return Boolean.TRUE.equals(subFields.get("E_COMMERCE_INDICATOR"));
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


}