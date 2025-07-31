package com.bbva.orchestrator.parser.refactor.utils;


import com.bbva.orchlib.parser.ParserException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


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


    public static BigDecimal conversionRateValidation(String value) {
        if (value == null || value.length() < 2) {
            throw new ParserException("El valor de la tasa de conversión es nulo o demasiado corto: " + value);
        }

        int precision = Character.getNumericValue(value.charAt(0));
        String digits = value.substring(1);

        if (precision < 0 || precision > 7 || precision > digits.length()) {
            throw new ParserException("Precisión inválida en la tasa de conversión: " + value);
        }

        StringBuilder sb = new StringBuilder(digits);
        sb.insert(sb.length() - precision, '.');
        try {
            return new BigDecimal(sb.toString());
        } catch (NumberFormatException e) {
            throw new ParserException("Error al parsear la tasa de conversión: " + value);
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
   /* public static Double conversionRateValidation(String rate) {
        Double value = parseDouble(rate);
        return (value != null && value == 0.0) ? null : value;
    }*/

    /**
     * Formatea fecha de expiración MMyy → MMyy
     */
    public static String convertFormatExpiryDate(String expiry) {
        return expiry != null && expiry.length() == 4 ? expiry : null;
    }
}