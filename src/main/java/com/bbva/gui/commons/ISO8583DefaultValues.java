package com.bbva.gui.commons;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public final class ISO8583DefaultValues {

    private static final Random RANDOM = new Random();

    private ISO8583DefaultValues() {
    }

    public static String getFor(int field, String tipoOperacion) {
        return switch (tipoOperacion) {
            case "Billetera" -> getForBilletera(field);
            case "Retiros" -> getForRetiros(field);
            default -> getForCompras(field);
        };
    }

    private static String getForBilletera(int i) {
        return switch (i) {
            case 2 -> "5536508888888888";
            case 3 -> "311000";
            case 4 -> "50.00";
            case 7 -> now("MMddHHmmss");
            case 11 -> randomStan();
            case 12 -> now("MMdd");
            case 13 -> now("HHmmss");
            case 14 -> "2701";
            default -> "";
        };
    }

    private static String getForRetiros(int i) {
        return switch (i) {
            case 2 -> "5536507777777777";
            case 3 -> "010000";
            case 4 -> "200.00";
            case 7 -> now("MMddHHmmss");
            case 11 -> randomStan();
            case 12 -> now("MMdd");
            case 13 -> now("HHmmss");
            case 14 -> "2701";
            default -> "";
        };
    }

    private static String getForCompras(int i) {
        return switch (i) {
            case 2 -> "5536509999999999";
            case 3 -> "000000";
            case 4 -> "100.00";
            case 7 -> now("MMddHHmmss");
            case 11 -> randomStan();
            case 12 -> now("MMdd");
            case 13 -> now("HHmmss");
            case 14 -> "2701";
            default -> "";
        };
    }

    private static String now(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    private static String randomStan() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }
}
