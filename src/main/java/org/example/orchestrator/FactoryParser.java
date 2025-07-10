package org.example.orchestrator;

public class FactoryParser {

    private FactoryParser() {
    }

    public static MessageParser getMessageParser() {

            return new MCMessageParserImpl();

}
}
