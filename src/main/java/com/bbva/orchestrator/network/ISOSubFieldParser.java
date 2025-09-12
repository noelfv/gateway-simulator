package com.bbva.orchestrator.network;

import com.bbva.orchestrator.core.dto.ISO8583;

import java.util.Map;

public interface ISOSubFieldParser{
    Map<String, String> parseSubfields(ISO8583 iso8583);
}
