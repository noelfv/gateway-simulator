package org.example.orchestrator.common;

import org.example.orchestrator.ParserException;

import java.nio.charset.Charset;
import java.util.HexFormat;
import java.util.Map;

public class ISOUtil {

    private ISOUtil() {
    }

    public static final Charset EBCDIC_CHARSET = Charset.forName("Cp1047");
    private static final HexFormat FORMATTER = HexFormat.of().withUpperCase();


    public static String getEnvVariableOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value != null) ? value : defaultValue;
    }

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

    public static String convertHEXtoEBCDIC(String valueHex) {
        byte[] ebcdic = hexStringToByteArray(valueHex);
        return new String(ebcdic, EBCDIC_CHARSET);
    }
    public static byte[] hexStringToByteArray(String s) {
        return HexFormat.of().parseHex(s);
    }

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

    public static String ebcdicToString(String message) {
        return new String(FORMATTER.parseHex(message), EBCDIC_CHARSET);
    }

    public static String validAmount(String amount) {
        if (amount == null || amount.isEmpty()) {
            return null;
        }
        String amountGeneral = amount.substring(0, amount.length() - 2);
        String amountCents = amount.substring(amount.length() - 2);
        return amountGeneral + "." + amountCents;
    }

    public static String convertToNumericString(int value, int radix, int length) {
        String numeroHex = Integer.toString(value, radix);
        String format = "%" + length + "s";
        String txtFormat = String.format(format, numeroHex).replace(' ', '0');

        return txtFormat.toUpperCase();
    }

    public static String revertValidAmount(String amount) {
        String revertedAmount = amount;
        if (amount.contains(",") || amount.contains(".")) {
            revertedAmount = amount.replace(",", "").trim();
            revertedAmount = revertedAmount.replace(".", "").trim();
        }

        return revertedAmount;

    }

    public static void main(String[] args) {
        System.out.println(convertBITMAPtoHEX("1111111011111111011001000000000110001000111000011110000100001010"));
    }
    public static String convertBITMAPtoHEX(String binaryBitmap) {
        StringBuilder hexBitmap = new StringBuilder();
        for (int i = 0; i < binaryBitmap.length(); i += 4) {
            String binary = binaryBitmap.substring(i, i + 4);
            hexBitmap.append(binChart.get(binary));
        }

        return hexBitmap.toString();
    }

    public static String convertToHexFormat(String value) {
        return HexFormat.of().withUpperCase().formatHex(value.getBytes(EBCDIC_CHARSET));
    }

    public static String convertToCharacter(String value) {
        return new String (HexFormat.of().parseHex(value),EBCDIC_CHARSET);
    }

    public static String convertEBCDICtoHEX(String valueEBCDIC) {
        byte[] bytes = valueEBCDIC.getBytes(EBCDIC_CHARSET);
        return HexFormat.of().withUpperCase().formatHex(bytes);
    }

    public static String stringToEBCDICHex(String input) {
        byte[] ebcdicBytes = input.getBytes(EBCDIC_CHARSET);
        return HexFormat.of().withUpperCase().formatHex(ebcdicBytes);
    }

    public static String processError(String messageIso, boolean containsSecondaryBitmap) {
        if(!containsSecondaryBitmap){
            return replaceWithF0(messageIso,28,24);
        }
        return replaceWithF0(messageIso,44,24);
    }

    /**
     * Reemplaza una sección de una trama con caracteres F0 a partir de una posición específica.
     * @param originalString La trama original
     * @param startPosition Posición de inicio (base 0)
     * @param charsToReplace Cantidad de caracteres a reemplazar
     * @return La trama con los caracteres reemplazados
     */
    public static String replaceWithF0(String originalString, int startPosition, int charsToReplace) {
        if (originalString == null || originalString.isEmpty()) {
            return originalString;
        }

        if (startPosition >= originalString.length()) {
            return originalString;
        }

        // Asegurar que no excedamos la longitud del string
        int endPosition = Math.min(startPosition + charsToReplace, originalString.length());

        // Construir el string de reemplazo con F0
        StringBuilder replacement = new StringBuilder();
        for (int i = 0; i < (endPosition - startPosition) / 2; i++) {
            replacement.append("F0");
        }

        // Si la cantidad de caracteres a reemplazar es impar, añadimos un F0 adicional
        if ((endPosition - startPosition) % 2 != 0) {
            replacement.append("F");
        }

        // Construir el string resultante
        return originalString.substring(0, startPosition) +
                replacement.toString() +
                originalString.substring(endPosition);
    }


    public static String padLeft(String inputString, int tamañoDeseado, char caracterRelleno) {
        if (inputString == null) {
            inputString = ""; // Asegurarse de que no sea null para evitar NullPointerException
        }

        // Si la cadena ya es del tamaño deseado o más grande, no hacemos nada
        if (inputString.length() >= tamañoDeseado) {
            return inputString;
        }

        // Crear un StringBuilder para construir la cadena de relleno
        StringBuilder sb = new StringBuilder();

        // Calcular cuántos caracteres de relleno necesitamos
        int numCaracteresRelleno = tamañoDeseado - inputString.length();

        // Añadir los caracteres de relleno al StringBuilder
        for (int i = 0; i < numCaracteresRelleno; i++) {
            sb.append(caracterRelleno);
        }

        // Añadir la cadena original después de los caracteres de relleno
        sb.append(inputString);

        return sb.toString();
    }

    public static boolean isHexadecimal(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c) && (c < 'A' || c > 'F') && (c < 'a' || c > 'f')) {
                return false;
            }
        }
        return true;
    }

    /**
     * Convierte una cadena hexadecimal a su representación ASCII
     * @param hex La cadena hexadecimal a convertir
     * @return La representación ASCII de la cadena hexadecimal
     */
    public static String hexToAscii(String hex) {
        if (hex == null || hex.isEmpty() || !isHexadecimal(hex)) {
            return "";
        }

        // Si la longitud es impar, añadir un 0 al inicio
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }

        // Convertir hex a bytes y luego a ASCII
        byte[] bytes = hexStringToByteArray(hex);
        return new String(bytes, Charset.forName("US-ASCII"));
    }
}

