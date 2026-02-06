package com.bbva.orchlib.parser;

import com.bbva.gateway.dto.iso20022.ISO20022;

public interface IParser {
    ISO20022 convert8583to20022(String jsonIso8583);
    String convert20022to8583(ISO20022 jsonIso20022);
}
