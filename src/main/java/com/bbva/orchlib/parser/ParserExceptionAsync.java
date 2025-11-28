package com.bbva.orchlib.parser;

/*
import com.bbva.business.gateway.proxy.Empty;
import com.bbva.business.gateway.proxy.MessageRequest;
import com.bbva.business.gateway.proxy.ProxyServiceGrpc;
import com.bbva.gateway.interceptors.GrpcClientRequestInterceptor;
*/

public class ParserExceptionAsync extends RuntimeException {
/*
    @Serial
    private static final long serialVersionUID = -7463267832719623980L;

    static GrpcConnectionPropertiesLoad grpcConnection;
    private transient ProxyServiceGrpc.ProxyServiceStub asyncStub;
    private transient ManagedChannel channel;

    public static void setGrpcConnectionPropertiesLoad(GrpcConnectionPropertiesLoad grpcConnection) {
        ParserExceptionAsync.grpcConnection = grpcConnection;
    }

    public ParserExceptionAsync(String message) {
        super(message);
        callProxyService(message);
    }

    public void callProxyService(String message) {
        String proxyServer = "";
        int proxyPort = 0;

        for (int i = 0; i < grpcConnection.getProcessor().size(); i++) {
            if (grpcConnection.getProcessor().get(i).getChannelServer().toLowerCase().contains(GrpcHeadersInfo.getNetwork().toLowerCase()) && grpcConnection.getProcessor().get(i).getChannelServer().toLowerCase().contains(GrpcHeadersInfo.getPort().toLowerCase())) {
                proxyServer = grpcConnection.getProcessor().get(i).getChannelServer();
                proxyPort = grpcConnection.getProcessor().get(i).getChannelPort();
                break;
            }
        }


        Context newContext = Context.current().fork();
        Context origContext = newContext.attach();
        try {
            LogsTraces.writeInfo("[ParserExceptionAsync] start call to Proxy (callProxyProcessorService)");

            if (channel == null || channel.isShutdown()) {
                createChannelAndStub(proxyServer, proxyPort);
            }

            MessageRequest req = MessageRequest.newBuilder().setMessage(message).build();
            asyncStub.postData(req, new StreamObserver<>() {
                @Override
                public void onNext(Empty value) {
                    // OnNext
                }

                @Override
                public void onError(Throwable t) {
                    LogsTraces.writeError("[ParserExceptionAsync] onError method "+ t.getMessage());
                }

                @Override
                public void onCompleted() {
                    // OnCompleted
                }
            });

        } finally {
            newContext.detach(origContext);
        }
    }

    public void createChannelAndStub(String serverGrpcClient, int portGrpcClient) {
        channel = ManagedChannelBuilder.forAddress(serverGrpcClient, portGrpcClient)
                .intercept(new GrpcClientRequestInterceptor())
                .usePlaintext().build();
        this.asyncStub = ProxyServiceGrpc.newStub(channel);
    }
    */

}
