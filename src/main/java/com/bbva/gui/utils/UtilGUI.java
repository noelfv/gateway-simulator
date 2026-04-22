package com.bbva.gui.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class UtilGUI {

    private static final Random RANDOM = new Random();

    public static int extractFieldIdFromLabel(String label) {
        try {
            return Integer.parseInt(label.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static boolean validateP002Field(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        if (!value.matches("\\d+")) {
            return false;
        }
        int length = value.length();
        return length >= 16 && length <= 19;
    }

    public static String padLeft(String inputString, int tamañoDeseado, char caracterRelleno) {
        if (inputString == null) {
            inputString = "";
        }
        if (inputString.length() >= tamañoDeseado) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        int numCaracteresRelleno = tamañoDeseado - inputString.length();
        for (int i = 0; i < numCaracteresRelleno; i++) {
            sb.append(caracterRelleno);
        }
        sb.append(inputString);
        return sb.toString();
    }

    public static String generateOperationDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss"));
    }

    public static String generateRandomSixDigitNumber() {
        int min = 111_111;
        int max = 999_999;
        return String.format("%06d", RANDOM.nextInt(max - min + 1) + min);
    }

    /** @deprecated Use FXUtils.showErrorAlert() instead */
    @Deprecated
    public static void showErrorDialog(String message) {
        FXUtils.showErrorAlert(message);
    }
}
