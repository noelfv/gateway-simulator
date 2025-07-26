package com.bbva.orchestrator.refactor;

public class utilsXXXX {
    public static class ISOUtil {
        // --- Métodos de Conversión Existentes (simulados) ---

        public static String ebcdicToString(String hexEbcdic) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hexEbcdic.length(); i += 2) {
                String hexByte = hexEbcdic.substring(i, i + 2);
                int decimalValue = Integer.parseInt(hexByte, 16);
                // Esto es una simplificación. La conversión real EBCDIC a ASCII es más compleja.
                sb.append((char) decimalValue);
            }
            return sb.toString().replace(" ", "");
        }

        public static String convertHEXtoEBCDIC(String hexValue) {
            return ebcdicToString(hexValue);
        }

        public static String convertHEXtoBITMAP(String hexBitmap) {
            StringBuilder binary = new StringBuilder();
            for (char c : hexBitmap.toCharArray()) {
                String bin = Integer.toBinaryString(Integer.parseInt(String.valueOf(c), 16));
                binary.append(String.format("%4s", bin).replace(' ', '0'));
            }
            return binary.toString();
        }

        public static String validAmount(String numericValue) {
            if (numericValue.length() < 2) return numericValue;
            return numericValue.substring(0, numericValue.length() - 2) + "." + numericValue.substring(numericValue.length() - 2);
        }

        public static String stringToEbcdicHex(String asciiString) {
            StringBuilder hexEbcdic = new StringBuilder();
            for (char c : asciiString.toCharArray()) {
                hexEbcdic.append(String.format("%02X", (int) c));
            }
            return hexEbcdic.toString();
        }

        public static String convertBinaryToHex(String binaryString) {
            StringBuilder hex = new StringBuilder();
            for (int i = 0; i < binaryString.length(); i += 4) {
                String nibble = binaryString.substring(i, Math.min(i + 4, binaryString.length()));
                hex.append(Integer.toHexString(Integer.parseInt(nibble, 2)).toUpperCase());
            }
            return hex.toString();
        }

        public static String ebcdicToHex(String ebcdicString) {
            StringBuilder hex = new StringBuilder();
            for (char c : ebcdicString.toCharArray()) {
                hex.append(String.format("%02X", (int) c));
            }
            return hex.toString();
        }

        public static String processError(String iso, boolean containsSecondaryBitmap) {
            return "Error context for ISO: " + iso.substring(0, Math.min(iso.length(), 50)) + "...";
        }

        // --- Nuevos Métodos Solicitados ---

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
                return "0"; // O lanzar una excepción, según la política de errores
            }
            try {
                // Long.parseLong(String s, int radix) es ideal para esto.
                // La base 16 es para hexadecimal.
                return String.valueOf(Long.parseLong(hex, 16));
            } catch (NumberFormatException e) {
                // Puedes loggear el error o relanzar una excepción personalizada
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
                return "0"; // O lanzar una excepción
            }
            try {
                // Long.toHexString(long l) convierte un long a su representación hexadecimal.
                // Usamos .toUpperCase() para asegurar que la salida sea consistente (letras mayúsculas).
                return Long.toHexString(Long.parseLong(decimal)).toUpperCase();
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Invalid decimal string: " + decimal + " - " + e.getMessage());
            }
        }
    }
}
