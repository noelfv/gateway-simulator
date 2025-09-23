package com.bbva.orchestrator.core.utils;

import com.bbva.orchestrator.core.fields.MastercardISOField;
import com.bbva.orchlib.parser.ParserException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

public class FieldUtil {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static boolean requiredProcess(String messageType) {
        return Set.of("0100","0120","0400","0420").contains(messageType);
    }
    /**
     * Parsea un String a Double, devuelve null si es nulo, vacío o inválido.
     */
    public static Double convertAmountDouble(String amount) {
        if (amount == null || amount.trim().isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(amount.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String convertAmountString(double amount) {
        // Usar BigDecimal para evitar problemas de precisión con double
        BigDecimal amountInCents = BigDecimal.valueOf(amount)
                .setScale(2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        // Formatea a 12 dígitos con ceros a la izquierda
        return String.format("%012d", amountInCents.longValue());
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
    public static String convertFormatDateTime(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            int year = LocalDate.now().getYear(); // year of the system
            String fullDate = year + value;  // yyyyMMddHHmmss

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime localDateTime = LocalDateTime.parse(fullDate, inputFormatter);
            Instant instant = localDateTime.atZone(ZoneOffset.UTC).toInstant();

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

            return outputFormatter.withZone(ZoneOffset.UTC).format(instant);
        } catch (Exception e) {
            return null;
        }
    }

    public static String reConvertFormatDateTime(String value) {
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

    public static String convertToLastDayOfMonth(String dateStr) {
        if (dateStr == null || dateStr.length() != 4) {
            return null;
        }

        try {
            // Extraer el año y el mes de la cadena de entrada
            int year = 2000 + Integer.parseInt(dateStr.substring(0, 2)); // "25" -> 2025
            int month = Integer.parseInt(dateStr.substring(2, 4)); // "04" -> abril

            if (month < 1 || month > 12) {
                return null; // Validar que el mes esté en el rango válido
            }

            // Crear el último día del mes
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

            // Formatear la salida como "yyyy-MM-dd"
            return lastDayOfMonth.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        } catch (NumberFormatException e) {
            return null; // Manejar errores de formato
        }
    }


    /**
     * Valida tasa de conversión: si es 000000, devuelve null.
     */
    public static Double conversionRateValidation(String rate) {
        Double value = convertAmountDouble(rate);
        return (value != null && value == 0.0) ? null : value;
    }

    /**
     * Formatea fecha de expiración MMyy → MMyy
     */
    public static String convertFormatExpiryDate(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMM");
            YearMonth yearMonth = YearMonth.parse(value, formatter);
            return yearMonth.toString();

        } catch (Exception e) {
            return null;
        }
    }

    public static String reConvertFormatExpiryDate(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        try {
            // Parsear la fecha en formato yyyy-MM
            YearMonth ym = YearMonth.parse(value);

            // Formatear a yyMM
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMM");
            return ym.format(formatter);
        } catch (Exception e) {
            return null;
        }
    }

    //TODO REVISAR LA FUNCIONALIDAD
    public static String convertEffectiveExchangeRate(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        StringBuilder conversionRate = new StringBuilder(value);
        int precision = Integer.parseInt(conversionRate.substring(0, 1));

        conversionRate.deleteCharAt(0);
        conversionRate.insert(conversionRate.length() - precision, ".");
        return conversionRate.toString();
    }

    //TODO REVISAR LA FUNCIONALIDAD
    public static String convertConversionRate(String value) {
        if (value == null) {
            return "";
        }

        StringBuilder conversionRate = new StringBuilder(value);
        while (conversionRate.length() < 8) {
            conversionRate.append("0");
        }

        int dotPosition = conversionRate.indexOf(".");
        int precision = conversionRate.length() - (dotPosition + 1);
        conversionRate.deleteCharAt(dotPosition);
        conversionRate.insert(0, precision);
        return conversionRate.toString();
    }

    // --- Métodos Específicos para Montos ---
    public static String validAmount(String amount) {
        if (amount == null || amount.isEmpty()) {
            return null;
        }
        String amountGeneral = amount.substring(0, amount.length() - 2);
        String amountCents = amount.substring(amount.length() - 2);
        return amountGeneral + "." + amountCents;
    }
    public static String revertValidAmount(String amount) {
        String revertedAmount = amount;
        if (amount.contains(",") || amount.contains(".")) {
            revertedAmount = amount.replace(",", "").trim();
            revertedAmount = revertedAmount.replace(".", "").trim();
        }
        return revertedAmount;
    }

    //TODO revisar el processError
    // --- Métodos para manejar errores de trama ---
    public static String processError(String messageIso,String network, boolean containsSecondaryBitmap) {
        if(!containsSecondaryBitmap){
            return replaceWithF0(messageIso,28,24);
        }
        return replaceWithF0(messageIso,44,24);
    }

    public static String formatMessageException(String code,String description,Throwable cause) {
        return String.format("Error Code: %s, Description: %s, Cause: %s", code, description, cause != null ? cause.getMessage() : "No cause provided");
    }

    public static String replaceWithF0(String originalString, int startPosition, int charsToReplace) {
        if (originalString == null || originalString.isEmpty()) {
            return originalString;
        }
        if (startPosition >= originalString.length()) {
            return originalString;
        }
        int endPosition = Math.min(startPosition + charsToReplace, originalString.length());
        StringBuilder replacement = new StringBuilder();
        for (int i = 0; i < charsToReplace / 2; i++) {
            replacement.append("F0");
        }
        if (charsToReplace % 2 != 0) {
            replacement.append("F");
        }
        return originalString.substring(0, startPosition) +
                replacement.toString() +
                originalString.substring(endPosition);
    }

    public static String getEnvVariableOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value != null) ? value : defaultValue;
    }

    public static String getValue(String fieldName, Map<String, String> values) {
        if (values.containsKey(fieldName)) {
            return values.get(fieldName);
        }
        return "";
    }

}