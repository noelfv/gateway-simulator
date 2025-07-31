package com.bbva.orchestrator.parser.refactor.utils;


import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class FieldProcessingService {

    public Double parseDouble(String value) {
        return FieldUtils.parseDouble(value);
    }



    public String isNullOrEmptySubstring(String source, int begin, int end) {
        return FieldUtils.isNullOrEmptySubstring(source, begin, end);
    }

    public String convertFormatDateTime(String input) {
        return FieldUtils.convertFormatDateTime(input);
    }

    /*public Double conversionRateValidation(String rate) {
        return FieldUtils.conversionRateValidation(rate);
    }*/

    public BigDecimal conversionRateValidation(String rate) {
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
}