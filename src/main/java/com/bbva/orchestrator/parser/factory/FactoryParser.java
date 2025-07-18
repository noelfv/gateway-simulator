package com.bbva.orchestrator.parser.factory;


import com.bbva.orchestrator.parser.factory.impl.MCMessageParserImpl;
import com.bbva.orchestrator.parser.factory.impl.VisaMessageParserImpl;

public class FactoryParser {

    public static final String RED_VISA = "peer01";
    public static final String RED_MASTERCARD = "peer02";

    private FactoryParser() {
    }

    public static MessageParser getMessageParser() {
       /* String networkName = GrpcHeadersInfo.getNetwork();

        if (networkName.equalsIgnoreCase(RED_VISA)) {
            return new VisaMessageParserImpl();
        } else if (networkName.equalsIgnoreCase(RED_MASTERCARD)) {
            return new MCMessageParserImpl();
        } else {
            throw new UnsupportedOperationException("UNSUPPORTED NETWORK");
        }*/
        return new MCMessageParserImpl();
    }


}
