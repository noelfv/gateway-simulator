package com.bbva.orchestrator.core.enums;

public enum NetworkName {

    VISA("PEER01"),
    PEER02("PEER02"),
    OTHER("");

    private final String code;

    NetworkName(String code) {
        this.code = code;
    }


}
