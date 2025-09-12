package com.bbva.orchestrator.core.logic;

import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FieldLogicFactory {

    private static final Map<String, String> PEER_TO_NETWORK = Map.of(
            "peer01", "visa",
            "peer02", "mastercard"
    );
    private final Map<String, NetworkDelegateFieldLogic> delegateFieldLogic;

    /**
     * Inyección automática de todos los beans que implementan ISO8583DelegateParser
     * Clave: nombre del bean (visaDelegateParser, mastercardDelegateParser, etc.)
     */
    public FieldLogicFactory(List<NetworkDelegateFieldLogic> parserList) {
        this.delegateFieldLogic = parserList.stream()
                .collect(Collectors.toMap(
                        p -> p.getClass().getSimpleName().replace("DelegateFieldLogic", "").toLowerCase(),
                        p -> p
                ));
    }

    /**
     * Obtiene el parser adecuado según el peerId pasado como parameter (ej. peer01 → Visa)
     */
    public NetworkDelegateFieldLogic getDelegateFieldLogic(String peerId) {
        if (peerId == null || peerId.isEmpty()) {
            return getDefaultFieldLogic();
        }

        String network = PEER_TO_NETWORK.get(peerId.toLowerCase());
        System.out.println(delegateFieldLogic);
        return delegateFieldLogic.getOrDefault(network, getDefaultFieldLogic());
    }

    /**
     * Obtiene el parser adecuado según el peerId del contexto de GrpcHeadersInfo
     * Si no se encuentra, devuelve el parser por defecto
     */
    public NetworkDelegateFieldLogic getDelegateFieldLogic() {
        String peerId= GrpcHeadersInfo.getNetwork();
        return getDelegateFieldLogic(peerId);
    }


    private NetworkDelegateFieldLogic getDefaultFieldLogic() {
        return delegateFieldLogic.getOrDefault("default",
                delegateFieldLogic.values().stream().findFirst().orElseThrow(
                        () -> new IllegalStateException("No hay ningún ISO8583DelegateParser disponible")
                ));
    }
}
