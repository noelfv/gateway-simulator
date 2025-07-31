package com.bbva.orchestrator.parser.refactor.iso20022.mapper.factory;


import com.bbva.orchestrator.parser.refactor.iso20022.mapper.ISO8583ToISO20022Mapper;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MapperFactory {

    private final Map<String, ISO8583ToISO20022Mapper> mappers;
    private static final Map<String, String> PEER_TO_NETWORK = Map.of(
            "PEER01", "Visa",
            "PEER02", "Mastercard"
    );

    public MapperFactory(Map<String, ISO8583ToISO20022Mapper> mapperMap) {
        this.mappers = new ConcurrentHashMap<>();
        mapperMap.forEach((beanName, mapper) -> {
            String simpleName = mapper.getClass().getSimpleName();
            // Extrae el nombre limpio: VisaISO8583ToISO20022Mapper → Visa
            String key = simpleName.replace("ISO8583ToISO20022Mapper", "");
            mappers.put(key, mapper);
        });
    }

    /**
     * Obtiene el mapper adecuado según el peerId (ej. PEER01, PEER02)
     * @param peerId Identificador del peer (ej. PEER01)
     * @return Mapper específico o default si no se encuentra
     */
    public ISO8583ToISO20022Mapper getMapper(String peerId) {
        if (peerId == null || peerId.isEmpty()) {
            return getDefaultMapper();
        }

        String network = PEER_TO_NETWORK.get(peerId.trim());
        if (network == null) {
            return getDefaultMapper();
        }

        // Normaliza: "Mastercard" → "Mastercard", pero el bean es "MasterCard..."
        String mapperKey = normalizeNetworkName(network);

        return mappers.getOrDefault(mapperKey, getDefaultMapper());
    }

    private String normalizeNetworkName(String network) {
        return switch (network.trim().toLowerCase()) {
            case "visa" -> "Visa";
            case "mastercard", "master card", "mc" -> "MasterCard"; // Ajusta según el nombre real del bean
            default -> network.trim();
        };
    }

    private ISO8583ToISO20022Mapper getDefaultMapper() {
        return mappers.getOrDefault("Default",
                mappers.values().stream().findFirst().orElse(null));
    }
}