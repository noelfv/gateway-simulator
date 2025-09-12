package com.bbva.orchestrator.core.utils;

import com.bbva.orchlib.parser.ParserException;

import java.nio.charset.Charset;
import java.util.HexFormat;
import java.util.Map;

public class ISOUtil {

    private ISOUtil() {
    }

    public static final Charset EBCDIC_CHARSET = Charset.forName("Cp1047");
    private static final HexFormat FORMATTER = HexFormat.of().withUpperCase();
    //public static final Charset EBCDIC_CHARSET = Charset.forName("IBM1047"); // Más estándar

    public static final Map<Character, String> hexChart = Map.ofEntries(
            Map.entry('0', "0000"),
            Map.entry('1', "0001"),
            Map.entry('2', "0010"),
            Map.entry('3', "0011"),
            Map.entry('4', "0100"),
            Map.entry('5', "0101"),
            Map.entry('6', "0110"),
            Map.entry('7', "0111"),
            Map.entry('8', "1000"),
            Map.entry('9', "1001"),
            Map.entry('A', "1010"),
            Map.entry('B', "1011"),
            Map.entry('C', "1100"),
            Map.entry('D', "1101"),
            Map.entry('E', "1110"),
            Map.entry('F', "1111")
    );

    public static final Map<String, Character> binChart = Map.ofEntries(
            Map.entry("0000", '0'),
            Map.entry("0001", '1'),
            Map.entry("0010", '2'),
            Map.entry("0011", '3'),
            Map.entry("0100", '4'),
            Map.entry("0101", '5'),
            Map.entry("0110", '6'),
            Map.entry("0111", '7'),
            Map.entry("1000", '8'),
            Map.entry("1001", '9'),
            Map.entry("1010", 'A'),
            Map.entry("1011", 'B'),
            Map.entry("1100", 'C'),
            Map.entry("1101", 'D'),
            Map.entry("1110", 'E'),
            Map.entry("1111", 'F')
    );

    // --- Métodos de Conversión EBCDIC Hex a String (ASCII) ---
    public static String convertHEXtoEBCDIC(String valueHex) {
        byte[] ebcdic = hexStringToByteArray(valueHex);
        return new String(ebcdic, EBCDIC_CHARSET);
    }
    public static byte[] hexStringToByteArray(String s) {
        return HexFormat.of().parseHex(s);
    }

    // --- Métodos de Conversión de Bitmap ---
    public static String convertHEXtoBITMAP(String valueHex) {
        StringBuilder binaryBitMap = new StringBuilder();
        for (char c : valueHex.toCharArray()) {
            if (!hexChart.containsKey(c)) {
                throw new ParserException("Malformed BitMap: " + binaryBitMap);
            }
            binaryBitMap.append(hexChart.get(c));
        }
        return String.valueOf(binaryBitMap);
    }
    public static String convertBITMAPtoHEX(String binaryBitmap) {
        StringBuilder hexBitmap = new StringBuilder();
        for (int i = 0; i < binaryBitmap.length(); i += 4) {
            String binary = binaryBitmap.substring(i, i + 4);
            hexBitmap.append(binChart.get(binary));
        }
        return hexBitmap.toString();
    }

    // --- Métodos de Conversión EBCDIC Hex a String (Numérico) ---
    public static String ebcdicToString(String messageHexEbcdic) {
        // Asume que 'messageHexEbcdic' es una cadena HEX EBCDIC
        return new String(FORMATTER.parseHex(messageHexEbcdic), EBCDIC_CHARSET);
    }

    //TODO revisar este metodo se parece al stringToEBCDICHex
    public static String convertToHexFormat(String value) {
        return HexFormat.of().withUpperCase().formatHex(value.getBytes(EBCDIC_CHARSET));
    }


    // --- Métodos de Conversión String (ASCII) a EBCDIC Hex ---
    public static String stringToEBCDICHex(String inputAscii) {
        byte[] ebcdicBytes = inputAscii.getBytes(EBCDIC_CHARSET);
        return HexFormat.of().withUpperCase().formatHex(ebcdicBytes);
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

    public static String processError(String messageIso, boolean containsSecondaryBitmap) {
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

    // --- Nuevos métodos para manejar ASCII plano directamente ---
    /**
     * Extrae una subcadena de una trama ASCII plana.
     * @param rawDataSegment La porción de la trama ASCII plana.
     * @param length La longitud en caracteres ASCII a extraer.
     * @return La subcadena ASCII extraída.
     */
    public static String extractAsciiString(String rawDataSegment, int length) {
        if (rawDataSegment.length() < length) {
            throw new ParserException("Trama ASCII demasiado corta. Esperado: " + length + ", Disponible: " + rawDataSegment.length());
        }
        return rawDataSegment.substring(0, length);
    }

    /**
     * Rellena una cadena ASCII a una longitud específica con espacios a la derecha.
     * @param value La cadena ASCII original.
     * @param length La longitud deseada.
     * @return La cadena ASCII rellena.
     */
    public static String padAsciiRight(String value, int length) {
        return String.format("%-" + length + "s", value).substring(0, length);
    }

    /**
     * Rellena una cadena ASCII a una longitud específica con ceros a la izquierda.
     * @param value La cadena ASCII original.
     * @param length La longitud deseada.
     * @return La cadena ASCII rellena.
     */
    public static String padAsciiLeft(String value, int length) {
        return String.format("%" + length + "s", value).replace(' ', '0').substring(0, length);
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