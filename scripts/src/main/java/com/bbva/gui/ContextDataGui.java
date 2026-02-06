package com.bbva.gui;

import com.bbva.gateway.interceptors.HeadersDTO;
// 1. Importa la clase GrpcHeadersInfo.
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import io.grpc.Context;
import org.springframework.stereotype.Component;

@Component
public class ContextDataGui {

    // Se elimina la clave local para usar la compartida.
    // public static final Context.Key<HeadersDTO> HEADERS_CONTEXT_KEY = Context.key("headers");

    private Context previousContext;

    public void initialize(HeadersDTO headers) {
        if (previousContext != null) {
            Context.current().detach(previousContext);
        }
        previousContext = Context.current().attach();
        // 2. Usa la clave 'HEADERS' importada de GrpcHeadersInfo.
        Context context = Context.current().withValue(GrpcHeadersInfo.HEADERS, headers);
        context.attach();
    }

    public void clear() {
        if (previousContext != null) {
            Context.current().detach(previousContext);
            previousContext = null;
        }
    }

    public HeadersDTO getHeaders() {
        // 3. Usa la misma clave 'HEADERS' para obtener los datos.
        return GrpcHeadersInfo.HEADERS.get();
    }
}