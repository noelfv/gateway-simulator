package com.bbva.gui.utils;


import com.bbva.orchestrator.network.mastercard.ISOFieldMastercard;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
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


    private void processMap0100(){
        Map<String,String> inputMessage = new HashMap<>();
        inputMessage.put("messageType", "0100");
        //campo 2
        inputMessage.put("primaryAccountNumber", "5536509999999999");
        //campo 3
        inputMessage.put("processingCode", "000000");
        //campo 4
        inputMessage.put("transactionAmount", "0000000229.90");
        //campo 5
        inputMessage.put("settlementAmount", "000000006356");
        //campo 6
        inputMessage.put("cardHolderBillingAmount", "0000000229.90");
        //campo 7
        inputMessage.put("transmissionDateTime", "0616072724");
        //campo 9
        inputMessage.put("conversionRateSettlement", "72764680");
        //campo 10
        inputMessage.put("conversionRate", "61000000");
        //campo 11
        inputMessage.put("systemTraceAuditNumber", "898716");
        //campo 12
        inputMessage.put("localTransactionTime", "101604");
        //campo 13
        inputMessage.put("localTransactionDate", "0715");
        //campo 14
        inputMessage.put("dateExpiration", "2905");
        //campo 15
        inputMessage.put("settlementDate", "0616");
        //campo 16
        inputMessage.put("dateConversion", "0615");
        //campo 18
        inputMessage.put("merchantType", "0001");
        //campo 20
        inputMessage.put("primaryAccountNumberCountryCode", "123");
        //campo 22
        inputMessage.put("pointServiceEntryMode", "123");
        //campo 26
        inputMessage.put("pointServicePIN", "12");
        //campo 32
        inputMessage.put("acquiringInstitutionIdentificationCode", "003286");
        // campo 33
        inputMessage.put("forwardingInstitutionIdentificationCode", "003286");
        // campo 35
        inputMessage.put("trackTwoData", "12345678901234567890=29051000000000000000");
        //campo 37
        inputMessage.put("retrievalReferenceNumber", "516754898716");
        //campo 41
        inputMessage.put("cardAcceptorTerminalIdentification", "00400216");
        //campo 42
        inputMessage.put("cardAcceptorIdentificationCode", "400216000108778");
        //campo 43
        inputMessage.put("cardAcceptorNameLocation", "APPLE.COM/BILL         866-712-7753  USA");
        //campo 44
        //inputMessage.put("additionalResponseData", "22");
        //campo 45
        inputMessage.put("trackOneData", "000000000000000000000000000000000000000");
        //campo 48
        inputMessage.put("additionalDataRetailer", "T37150511000009999974207010321022080504M1036105000015618AQV116AQS609AQF116753201038800202140303880040214050200710418C ");
        //campo 49
        inputMessage.put("transactionCurrencyCode", "604");
        //campo 50
        inputMessage.put("settlementCurrencyCode", "840");
        //campo 51
        inputMessage.put("cardholderBillingCurrencyCode", "604");
        //campo 52
        inputMessage.put("pinData", "ABCDEF12");
        //campo 54
        inputMessage.put("additionalAmounts", "123");
        //campo 55
        inputMessage.put("integratedCircuitCard", "123");
        //campo 61
        inputMessage.put("posCardIssuer", "000410000060084095014     ");
        //campo 62
        inputMessage.put("postalCode", "123");
        //campo 63
        inputMessage.put("networkData", "123");
        //campo 73
        inputMessage.put("dateAction", "123456");
        //campo 95
        inputMessage.put("replacementAmounts", "000000000000000000000000000000000000000000");
        //campo 112
        inputMessage.put("additionalDataNationalUse", "123");
        //campo 120
        inputMessage.put("keyManagement", "123");
        // campo 121
        //inputMessage.put("authorizingAgentIdCode", "123");
        //campo 127
        inputMessage.put("privateData", "123");

        //String trama=messageParser.unParser(inputMessage, true);
        //outputTextArea.setText(trama);
    }


    private void processMap0110(){
        Map<String,String> inputMessage = new HashMap<>();
        inputMessage.put("messageType", "0110");
        //campo 2
        inputMessage.put("primaryAccountNumber", "5536509999999999");
        //campo 3
        inputMessage.put("processingCode", "000000");
        //campo 4
        inputMessage.put("transactionAmount", "0000000229.90");
        //campo 5
        inputMessage.put("settlementAmount", "000000006356");
        //campo 6
        inputMessage.put("cardHolderBillingAmount", "0000000229.90");
        //campo 7
        inputMessage.put("transmissionDateTime", "0616072724");
        //campo 9
        inputMessage.put("conversionRateSettlement", "72764680");
        //campo 10
        inputMessage.put("conversionRate", "61000000");
        //campo 11
        inputMessage.put("systemTraceAuditNumber", "898716");
        //campo 12
        //inputMessage.put("localTransactionTime", "061604");
        //campo 15
        inputMessage.put("settlementDate", "0616");
        //campo 16
        inputMessage.put("dateConversion", "0615");
        //campo 20
        inputMessage.put("primaryAccountNumberCountryCode", "123");
        //campo 32
        inputMessage.put("acquiringInstitutionIdentificationCode", "003286");
        // campo 33
        inputMessage.put("forwardingInstitutionIdentificationCode", "003286");
        //campo 37
        inputMessage.put("retrievalReferenceNumber", "516754898716");
        //campo 38
        inputMessage.put("authorizationIdentificationResponse", "012345");
        //campo 39
        inputMessage.put("responseCode", "00");
        //campo 41
        inputMessage.put("cardAcceptorTerminalIdentification", "00400216");
        //campo 44
        inputMessage.put("additionalResponseData", "22");
        //campo 48
        inputMessage.put("additionalDataRetailer", "T37150511000009999974207010321022080504M1036105000015618AQV116AQS609AQF116753201038800202140303880040214050200710418C ");
        //campo 49
        inputMessage.put("transactionCurrencyCode", "604");
        //campo 50
        inputMessage.put("settlementCurrencyCode", "840");
        //campo 51
        inputMessage.put("cardholderBillingCurrencyCode", "604");
        //campo 54
        inputMessage.put("additionalAmounts", "123");
        //campo 55
        inputMessage.put("integratedCircuitCard", "123");
        //campo 62
        inputMessage.put("postalCode", "123");
        //campo 63
        inputMessage.put("networkData", "123");
        //campo 112
        inputMessage.put("additionalDataNationalUse", "123");
        //campo 120
        inputMessage.put("keyManagement", "123");
        //campo 121
        inputMessage.put("authorizingAgentIdCode", "123");
        //campo 127
        inputMessage.put("privateData", "123");

       // String trama=messageParser.unParser(inputMessage, true);
        //outputTextArea.setText(trama);
    }



}
