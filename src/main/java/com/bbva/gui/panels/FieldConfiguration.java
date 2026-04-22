package com.bbva.gui.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class FieldConfiguration {

    public static final List<Integer> COMPRAS = List.of(
            2, 3, 4, 5, 6, 7, 11, 12, 13, 14, 18, 19,
            22, 25, 32, 33, 37, 41, 42, 43, 48, 49, 54, 59, 60, 61, 62, 104, 122);

    public static final List<Integer> BILLETERA = List.of(
            2, 3, 4, 5, 6, 7, 11, 12, 13, 14, 18, 19,
            22, 25, 32, 33, 37, 41, 42, 43, 48, 49, 70);

    public static final List<Integer> RETIROS = List.of(
            2, 3, 4, 5, 6, 7, 11, 12, 13, 14, 18, 19,
            22, 25, 32, 33, 37, 41, 42, 43, 48, 49, 54, 59, 60, 61, 62, 63, 70);

    public static final Set<Integer> MANDATORIOS_COMPRAS = Set.of(
            2, 3, 4, 5, 6, 7, 11, 12, 13, 14, 18, 19,
            22, 25, 32, 33, 37, 41, 42, 43, 48, 49, 54, 59, 60, 61, 62, 104, 122);

    public static final Set<Integer> MANDATORIOS_BILLETERA = Set.of(
            2, 3, 4, 5, 6, 7, 11, 12, 13, 14, 18, 19,
            22, 25, 32, 33, 37, 41, 42, 43, 48, 49, 70);

    public static final Set<Integer> MANDATORIOS_RETIROS = Set.of(
            2, 3, 4, 5, 6, 7, 11, 12, 13, 14, 18, 19,
            22, 25, 32, 33, 37, 41, 42, 43, 48, 49, 54, 59, 60, 61, 62, 63, 70);

    private FieldConfiguration() {}

    public static List<Integer> getCampos(String tipo) {
        return switch (tipo) {
            case "Billetera" -> new ArrayList<>(BILLETERA);
            case "Retiros"   -> new ArrayList<>(RETIROS);
            default          -> new ArrayList<>(COMPRAS);
        };
    }

    public static Set<Integer> getMandatorios(String tipo) {
        return switch (tipo) {
            case "Billetera" -> MANDATORIOS_BILLETERA;
            case "Retiros"   -> MANDATORIOS_RETIROS;
            default          -> MANDATORIOS_COMPRAS;
        };
    }
}
