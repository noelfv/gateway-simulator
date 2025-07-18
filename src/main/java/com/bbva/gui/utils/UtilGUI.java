package com.bbva.gui.utils;


import com.bbva.orchestrator.network.mastercard.ISOFieldMastercard;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

public class UtilGUI {


    public static int extractFieldIdFromLabel(String label) {
        try {
            // Elimina la "P" y convierte a entero
            return Integer.parseInt(label.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return -1; // o lanza una excepción si prefieres
        }
    }

    public static boolean validateP002Field(String value) {
        if (value == null || value.isBlank()) {
            return false; // Permitir campos vacíos
        }

        // Validar que solo contenga números
        if (!value.matches("\\d+")) {
            return false;
        }

        // Validar longitud del PAN (13-19 dígitos)
        int length = value.length();
        return length >= 16 && length <= 19;
    }

    public static boolean validateFieldValue(String fieldLabel, String value) {
        if (value == null || value.isBlank()) {
            return true; // Permitir campos vacíos
        }

        int fieldId = extractFieldIdFromLabel(fieldLabel);

        // Buscar información del campo en ISOFieldMastercard
        Optional<ISOFieldMastercard> fieldInfo = ISOFieldMastercard.findById(fieldId);

        if (fieldInfo.isPresent()) {
            ISOFieldMastercard field = fieldInfo.get();

            // Validar según el tipo de campo
            switch (fieldId) {
                case 2: // PAN
                    return validateP002Field(value);
                case 3: // Processing Code
                    return value.matches("\\d{6}") && value.matches("\\d+");
                case 4: // Transaction Amount
                    return value.matches("\\d{1,12}");
                case 11: // System Trace Audit Number
                    return value.matches("\\d{6}");
                case 12: // Local Transaction Time
                    return value.matches("\\d{6}");
                case 13: // Local Transaction Date
                    return value.matches("\\d{4}");
                // Agregar más validaciones según necesites
                default:
                    return true; // Para campos sin validación específica
            }
        }

        return true;
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


    private String getFieldValidationError(String fieldLabel, String value) {
        if (value == null || value.isBlank()) {
            return null; // No hay error para campos vacíos
        }

        int fieldId = extractFieldIdFromLabel(fieldLabel);
        Optional<ISOFieldMastercard> fieldInfo = ISOFieldMastercard.findById(fieldId);

        if (fieldInfo.isPresent()) {
            ISOFieldMastercard field = fieldInfo.get();

            switch (fieldId) {
                case 2:
                    if (!value.matches("\\d+")) {
                        return "El PAN debe contener solo números";
                    }
                    if (value.length() < 13 || value.length() > 19) {
                        return "El PAN debe tener entre 13 y 19 dígitos";
                    }
                    break;
                case 3:
                    if (!value.matches("\\d{6}")) {
                        return "El Processing Code debe tener exactamente 6 dígitos";
                    }
                    break;
                // Agregar más validaciones específicas
            }
        }

        return null; // No hay error
    }


    public static String generateOperationDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmmss");
        return now.format(formatter);
    }

    public static String generateRandomSixDigitNumber() {
        Random random = new Random();
        int min = 111111;
        int max = 999999;
        int randomNumber = random.nextInt(max - min + 1) + min;
        return String.format("%06d", randomNumber);
    }


    public static void showErrorDialog(String message) {
        // Limitar mensaje a 150 caracteres y usar HTML para wrap
        String displayMessage = message.length() > 200 ?
                message.substring(0, 200) + "..." : message;

        String htmlMessage = "<html><body style='width: 250px; padding: 10px;'>" +
                displayMessage.replace("\n", "<br>") + "</body></html>";

        JOptionPane.showMessageDialog(null, htmlMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
