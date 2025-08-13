package com.bbva.orchestrator.parser.refactor.mapper.factory;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MapperFactory {

    private final Map<String, ISO20022DelegateMapper> mappers;
    /**
     * Mapa que relaciona peerId con el nombre de la red correspondiente
     * Ejemplo: "PEER01" → "Visa", "PEER02" → "Mastercard"
     */
    private static final Map<String, String> PEER_TO_NETWORK = Map.of(
            "peer01", "visa",
            "peer02", "mastercard"
    );

    /**
     * Inyección automática de todos los beans que implementan ISO20022DelegateMapper
     * Clave: nombre del bean (visaDelegateMapper, mastercardDelegateMapper, etc.)
     */
    public MapperFactory(List<ISO20022DelegateMapper> mapperList) {
        this.mappers = mapperList.stream()
                .collect(Collectors.toMap(
                        p -> p.getClass().getSimpleName().replace("DelegateMapper", "").toLowerCase(),
                        p -> p
                ));
    }

    /**
     * Obtiene el mapper adecuado según el peerId (ej. PEER01, PEER02)
     * @param peerId Identificador del peer (ej. PEER01)
     * @return Mapper específico o default si no se encuentra
     */
    public ISO20022DelegateMapper getDelegateMapper(String peerId) {
        if (peerId == null || peerId.isEmpty()) {
            return getDefaultMapper();
        }

        String network = PEER_TO_NETWORK.get(peerId.toLowerCase());
        if (network == null) {
            return getDefaultMapper();
        }

        return mappers.getOrDefault(network, getDefaultMapper());
    }


    private ISO20022DelegateMapper getDefaultMapper() {
        return mappers.getOrDefault("default",
                mappers.values().stream().findFirst().orElseThrow(() -> new IllegalStateException("No hay ningún ISO20022DelegateMapper disponible")));
    }
}