package com.bbva.orchestrator.core.utils;

import com.bbva.orchestrator.core.fields.MastercardISOField;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

public class FieldUtils {

    public static final String FIELD_NOT_MAPPED = "NOT_MAPPED";
    public static final String TYPE_MESSAGE = "TYPE_MESSAGE";

    /**
     * Parsea un String a Double, devuelve null si es nulo, vacío o inválido.
     */
    public static Double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Extrae un substring seguro. Si el índice está fuera de rango, devuelve null.
     */
    public static String isNullOrEmptySubstring(String source, int beginIndex, int endIndex) {
        if (source == null || source.length() < endIndex) {
            return null;
        }
        String substring = source.substring(beginIndex, endIndex);
        return substring.isEmpty() ? null : substring;
    }

    /**
     * Convierte fecha-hora de formato MMddHHmmss a yyyyMMddHHmmss
     */
    public static String convertFormatDateTime(String input) {
        if (input == null || input.length() != 10) return null;
        try {
            String month = input.substring(0, 2);
            String day = input.substring(2, 4);
            String hour = input.substring(4, 6);
            String minute = input.substring(6, 8);
            String second = input.substring(8, 10);
            LocalDateTime dateTime = LocalDateTime.of(2000, Integer.parseInt(month),
                    Integer.parseInt(day), Integer.parseInt(hour),
                    Integer.parseInt(minute), Integer.parseInt(second));
            return dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        } catch (Exception e) {
            return input; // fallback
        }
    }

    /**
     * Valida tasa de conversión: si es 000000, devuelve null.
     */
    public static Double conversionRateValidation(String rate) {
        Double value = parseDouble(rate);
        return (value != null && value == 0.0) ? null : value;
    }

    /**
     * Formatea fecha de expiración MMyy → MMyy
     */
    public static String convertFormatExpiryDate(String expiry) {
        return expiry != null && expiry.length() == 4 ? expiry : null;
    }




    public static String unParserPlainText(Map<String, String> mapValues) {
        StringBuilder binaryBitmap = new StringBuilder();
        binaryBitmap.append('0'); // Bit 1 del bitmap primario
        StringBuilder isoValues = new StringBuilder();

        boolean hasSecondaryBitmap = false;

        for (int i = 2; i <= 128; i++) {

            MastercardISOField field = MastercardISOField.getById(i);

            if (field == null) {
                binaryBitmap.append('0');
                continue;
            }

            String fieldName = field.getName();

            String value = mapValues.get(fieldName);

            if (value != null && !value.isEmpty()) {
                binaryBitmap.append('1');
                isoValues.append(value); // Valor en claro, sin EBCDIC, sin padding

                if (i > 64) {
                    hasSecondaryBitmap = true;
                }
            } else {
                binaryBitmap.append('0');
            }
        }

        if (hasSecondaryBitmap) {
            binaryBitmap.setCharAt(0, '1');
        } else {
            binaryBitmap.setLength(64);
        }

        String messageType = mapValues.getOrDefault("messageType", "0000");
        String bitmapHex = ISOUtil.convertBITMAPtoHEX(binaryBitmap.toString());

        return messageType + bitmapHex + isoValues;
    }

    public static String convertDoubleToString(Double value, int padding) {
        String format = "%0" + (padding + 1) + ".2f";
        return (value == null) ? "" : String.format(format, value);
    }

    public static String getConversionRate(String value, int padding) {
        if (value == null) {
            return "";
        }

        StringBuilder conversionRate = new StringBuilder(value);
        while (conversionRate.length() < padding) {
            conversionRate.append("0");
        }

        int dotPosition = conversionRate.indexOf(".");
        int precision = conversionRate.length() - (dotPosition + 1);
        conversionRate.deleteCharAt(dotPosition);
        conversionRate.insert(0, precision);
        return conversionRate.toString();
    }

    public static String convertFormatDateTime2(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            // Parseando la fecha ISO con zona horaria Z (UTC)
            Instant instant = Instant.parse(value);
            ZonedDateTime zonedDateTime = instant.atZone(ZoneOffset.UTC);

            // Formateo al patrón MMddHHmmss
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMddHHmmss");

            return zonedDateTime.format(outputFormatter);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isFlowAsynchronous(String typeMessage) {
        return Set.of("0110","0130","0410","0430").contains(typeMessage);
    }

}