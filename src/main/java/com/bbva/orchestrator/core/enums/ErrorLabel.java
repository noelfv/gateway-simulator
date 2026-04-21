package com.bbva.orchestrator.core.enums;

import lombok.Getter;

import java.util.*;

/**
 * Enum que centraliza las etiquetas de error reconocidas en el flujo del gateway.
 * Cada constante define un prefijo de error y su descripción para monitoreo/traza.
 * <p>
 * Para agregar un nuevo tipo de error, basta con añadir una nueva constante al enum.
 */
@Getter
public enum ErrorLabel {

    GW_ERROR_PROXY_INVALID_CHARSET("gw_error_proxyInvalidCharset", "Error al decodificar el paquete de tramas","PROXY"),
    GW_ERROR_PROXY_INVALID_LENGTH("gw_error_proxyInvalidLength", "Error al leerse el indicador de largo de la trama","PROXY"),
    GW_ERROR_PROXY_OUT_OF_RANGE("gw_error_proxyOutOfRangeLength", "largo extraído esta fuera del rango de caracteres de una trama válida","PROXY"),
    GW_ERROR_PROXY_INVALID_MESSAGE("gw_error_proxyInvalidMessage", "Error al extraer el resto de campos del mensaje","PROXY"),
    GW_ERROR_PROXY_GARBAGE("gw_error_proxyGarbage", "Se han detectado caracteres basura en el paquete de tramas","PROXY"),
    GW_ERROR_INTERNAL_PROXY("gw_error_internalProxy", "Error inesperado en lógica interna del microservicio Proxy","PROXY"),
    GW_ERROR_ORCHESTRATOR_PARSER("gw_error_orchestratorParser", "Error al realizar el parseo de la trama en el orquestador", "ORCHESTRATOR"),
    GW_ERROR_ORCHESTRATOR_UNPARSER("gw_error_orchestratorUnparser", "Error al realizar el desparseo de la trama en el orquestador", "ORCHESTRATOR");

    /** Map de key exacta para búsqueda O(1) */
    private static final Map<String, ErrorLabel> EXACT_MAP;

    /** Keys ordenadas de mayor a menor longitud para fallback con startsWith */
    private static final List<Map.Entry<String, ErrorLabel>> SORTED_ENTRIES;

    static {
        Map<String, ErrorLabel> map = new HashMap<>();
        for (ErrorLabel e : values()) {
            map.put(e.key, e);
        }
        EXACT_MAP = Collections.unmodifiableMap(map);

        List<Map.Entry<String, ErrorLabel>> entries = new ArrayList<>(map.entrySet());
        entries.sort((a, b) -> Integer.compare(b.getKey().length(), a.getKey().length()));
        SORTED_ENTRIES = Collections.unmodifiableList(entries);
    }

    private final String key;
    private final String value;
    private final String component;

    ErrorLabel(String key, String value, String component) {
        this.key = key;
        this.value = value;
        this.component = component;
    }

    /**
     * Resuelve un label a su ErrorLabel correspondiente.
     * Primero intenta coincidencia exacta O(1) vía HashMap.
     * Si no hay match exacto, busca por prefijo (startsWith) con prioridad
     * al match más largo para evitar colisiones de prefijos.
     *
     * @param label la etiqueta recibida (puede ser null)
     * @return Optional con el ErrorLabel si se encuentra coincidencia, vacío si no
     */
    public static Optional<ErrorLabel> fromLabel(String label) {
        if (label == null || label.isEmpty()) {
            return Optional.empty();
        }
        // Búsqueda O(1) por coincidencia exacta (caso más frecuente)
        ErrorLabel exact = EXACT_MAP.get(label);
        if (exact != null) {
            return Optional.of(exact);
        }
        // Fallback: búsqueda por prefijo, ya ordenado de mayor a menor longitud
        for (Map.Entry<String, ErrorLabel> entry : SORTED_ENTRIES) {
            if (label.startsWith(entry.getKey())) {
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
    }

    /**
     * Verifica si un label corresponde a cualquier etiqueta de error conocida.
     *
     * @param label la etiqueta a evaluar
     * @return true si el label es un error conocido
     */
    public static boolean isErrorLabel(String label) {
        return fromLabel(label).isPresent();
    }
}
