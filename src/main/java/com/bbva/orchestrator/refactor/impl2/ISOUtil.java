package com.bbva.orchestrator.refactor.impl2;

public class ISOUtil {

    // --- Métodos de Conversión Existentes (simulados y corregidos para EBCDIC numérico) ---

    /**
     * Convierte una cadena hexadecimal EBCDIC a su representación de cadena ASCII.
     * Corregido para manejar dígitos numéricos EBCDIC (F0-F9) correctamente.
     * Nota: Para caracteres no numéricos EBCDIC, esta es una simplificación; una implementación
     * completa requeriría una tabla de mapeo EBCDIC a ASCII.
     * @param hexEbcdic La cadena de entrada en formato hexadecimal EBCDIC (ej. "F0F1F2F3" para "0123").
     * @return La cadena ASCII resultante.
     */
    public static String ebcdicToString(String hexEbcdic) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hexEbcdic.length(); i += 2) {
            String hexByte = hexEbcdic.substring(i, i + 2);
            int decimalValue = Integer.parseInt(hexByte, 16);

            // Manejo específico para dígitos EBCDIC (F0-F9)
            if (decimalValue >= 0xF0 && decimalValue <= 0xF9) {
                sb.append((char) ('0' + (decimalValue - 0xF0)));
            } else {
                // Para otros caracteres EBCDIC, esta es una simplificación.
                // Una implementación real necesitaría un mapeo completo.
                // Podrías mapear a un carácter de reemplazo o a un espacio si no se reconoce.
                // Por ahora, se mantiene la conversión directa que puede producir "caracteres extraños".
                sb.append((char) decimalValue);
            }
        }
        return sb.toString();
    }

    /**
     * Convierte una cadena hexadecimal a su representación de cadena EBCDIC (ASCII).
     * En este contexto simulado, es similar a ebcdicToString si el input es EBCDIC HEX.
     * @param hexValue La cadena hexadecimal de entrada.
     * @return La cadena EBCDIC (ASCII) resultante.
     */
    public static String convertHEXtoEBCDIC(String hexValue) {
        // Para campos ALPHA_NUMERIC, esta función es la que se usa.
        // Si los campos ALPHA_NUMERIC también usan EBCDIC, esta función debe usar el mapeo completo.
        // Por ahora, delega a ebcdicToString que maneja los numéricos.
        return ebcdicToString(hexValue);
    }

    /**
     * Convierte una cadena hexadecimal que representa un bitmap a su representación binaria (cadena de '0's y '1's).
     * @param hexBitmap La cadena hexadecimal del bitmap (ej. "8000" para el bit 1 activo).
     * @return La cadena binaria resultante.
     */
    public static String convertHEXtoBITMAP(String hexBitmap) {
        StringBuilder binary = new StringBuilder();
        for (char c : hexBitmap.toCharArray()) {
            String bin = Integer.toBinaryString(Integer.parseInt(String.valueOf(c), 16));
            binary.append(String.format("%4s", bin).replace(' ', '0'));
        }
        return binary.toString();
    }

    /**
     * Valida y formatea una cadena numérica que representa un monto.
     * Asume que los últimos dos dígitos son decimales.
     * @param numericValue La cadena numérica de entrada (ej. "000000123456").
     * @return El monto formateado con un punto decimal (ej. "1234.56").
     */
    public static String validAmount(String numericValue) {
        if (numericValue == null || numericValue.length() < 2) {
            return numericValue; // O manejar error, o devolver "0.00"
        }
        // Asegurarse de que el valor sea solo numérico antes de formatear
        if (!numericValue.matches("\\d+")) {
            return numericValue; // O lanzar una excepción si no es numérico
        }
        return numericValue.substring(0, numericValue.length() - 2) + "." + numericValue.substring(numericValue.length() - 2);
    }

    /**
     * Convierte una cadena ASCII a su representación hexadecimal EBCDIC.
     * Corregido para manejar dígitos numéricos ASCII ('0'-'9') correctamente.
     * Nota: Para caracteres no numéricos ASCII, esta es una simplificación; una implementación
     * completa requeriría una tabla de mapeo ASCII a EBCDIC.
     * @param asciiString La cadena ASCII de entrada.
     * @return La cadena hexadecimal EBCDIC resultante.
     */
    public static String stringToEbcdicHex(String asciiString) {
        StringBuilder hexEbcdic = new StringBuilder();
        for (char c : asciiString.toCharArray()) {
            // Manejo específico para dígitos ASCII ('0'-'9')
            if (c >= '0' && c <= '9') {
                hexEbcdic.append(String.format("%02X", (int) c + 0xF0 - (int) '0'));
            } else {
                // Para otros caracteres ASCII, esta es una simplificación.
                // Una implementación real necesitaría un mapeo completo.
                hexEbcdic.append(String.format("%02X", (int) c));
            }
        }
        return hexEbcdic.toString();
    }

    /**
     * Convierte una cadena binaria (ej. "0101") a su representación hexadecimal.
     * @param binaryString La cadena binaria de entrada.
     * @return La cadena hexadecimal resultante.
     */
    public static String convertBinaryToHex(String binaryString) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < binaryString.length(); i += 4) {
            String nibble = binaryString.substring(i, Math.min(i + 4, binaryString.length()));
            hex.append(Integer.toHexString(Integer.parseInt(nibble, 2)).toUpperCase());
        }
        return hex.toString();
    }

    /**
     * Convierte una cadena EBCDIC (interpretada como ASCII aquí) a su representación hexadecimal.
     * @param ebcdicString La cadena EBCDIC de entrada.
     * @return La cadena hexadecimal resultante.
     */
    public static String ebcdicToHex(String ebcdicString) {
        // En este contexto, es similar a stringToEbcdicHex si el input es ASCII.
        // Si ebcdicString ya es la representación de caracteres EBCDIC, esta función
        // debería simplemente convertir esos caracteres a sus valores hexadecimales correspondientes.
        // Asumiendo que ebcdicString ya es el valor decodificado (ej. "0100"),
        // entonces se usa stringToEbcdicHex para convertirlo a F0F1F0F0.
        return stringToEbcdicHex(ebcdicString);
    }

    /**
     * Procesa y devuelve un contexto de error para una trama ISO.
     * @param iso La trama ISO completa.
     * @param containsSecondaryBitmap Indica si la trama contiene un bitmap secundario.
     * @return Una cadena que describe el contexto del error.
     */
    public static String processError(String iso, boolean containsSecondaryBitmap) {
        return "Error context for ISO: " + iso.substring(0, Math.min(iso.length(), 50)) + "...";
    }

    // --- Métodos de Conversión Hexadecimal y Decimal ---

    /**
     * Convierte una cadena hexadecimal a su representación decimal como cadena.
     * Maneja números grandes usando Long.
     *
     * @param hex La cadena hexadecimal a convertir (ej. "A", "FF", "1A2B").
     * @return La representación decimal de la cadena hexadecimal (ej. "10", "255", "6700").
     * @throws NumberFormatException Si la cadena de entrada no es un número hexadecimal válido.
     */
    public static String hexToDecimal(String hex) {
        if (hex == null || hex.isEmpty()) {
            return "0";
        }
        try {
            return String.valueOf(Long.parseLong(hex, 16));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid hexadecimal string: " + hex + " - " + e.getMessage());
        }
    }

    /**
     * Convierte una cadena decimal a su representación hexadecimal como cadena.
     * Maneja números grandes usando Long.
     *
     * @param decimal La cadena decimal a convertir (ej. "10", "255", "6700").
     * @return La representación hexadecimal de la cadena decimal (ej. "A", "FF", "1A2C").
     * La cadena resultante estará en mayúsculas.
     * @throws NumberFormatException Si la cadena de entrada no es un número decimal válido.
     */
    public static String decimalToHex(String decimal) {
        if (decimal == null || decimal.isEmpty()) {
            return "0";
        }
        try {
            return Long.toHexString(Long.parseLong(decimal)).toUpperCase();
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid decimal string: " + decimal + " - " + e.getMessage());
        }
    }
}
