package com.bbva.orchestrator.core.mapper.iso20022.strategy.impl;

import com.bbva.gateway.dto.iso20022.MacDataDTO;
import com.bbva.gateway.dto.iso20022.SecurityTrailerDTO;
import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.mapper.iso20022.strategy.SectionMappingStrategy;
import com.bbva.orchestrator.core.utils.FieldProcessingService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SecurityTrailerMappingStrategy implements SectionMappingStrategy<SecurityTrailerDTO> {

    private final FieldProcessingService fieldService;

    public SecurityTrailerMappingStrategy(FieldProcessingService fieldService) {
        this.fieldService = fieldService;
    }

    @Override
    public SecurityTrailerDTO mapper(ISO8583 input, Map<String, String> subFields) {

        String secControlInformation = input.getSecurityControlInformation();

        if (secControlInformation == null || secControlInformation.isEmpty()) {
            return null; // Retorna null si no hay información de control de seguridad
        }

        MacDataDTO macData = MacDataDTO.builder()
                // 53.1 Security Format Code / Security Type Code
                .keyProtection(fieldService.isNullOrEmptySubstring(secControlInformation, 0, 2))
                // 53.2 PIN Encryption Code
                .algorithm(fieldService.isNullOrEmptySubstring(secControlInformation, 2, 4))
                // 53.3 PIN Block Format Code
                .derivedInformation(fieldService.isNullOrEmptySubstring(secControlInformation, 4, 6))
                // 53.4 Key Index Number
                .keyIndex(fieldService.isNullOrEmptySubstring(secControlInformation, 6, secControlInformation.length()))
                .build();

        return SecurityTrailerDTO.builder()
                .macData(macData)
                .build();
    }

    @Override
    public Map<String, String> unMapper(SecurityTrailerDTO input) {
        Map<String, String> mapValues = new HashMap<>();

        /*MacDataDTO macData = input.getMacData();

        String securityControlInfo = fieldService.getFieldValue(macData, MacDataDTO::getKeyProtection, DEFAULT_EMPTY_VALUE) +
                fieldService.getFieldValue(macData, MacDataDTO::getAlgorithm, DEFAULT_EMPTY_VALUE) +
                fieldService.getFieldValue(macData, MacDataDTO::getDerivedInformation, DEFAULT_EMPTY_VALUE) +
                fieldService.getFieldValue(macData, MacDataDTO::getKeyIndex, DEFAULT_EMPTY_VALUE);

        mapValues.put("securityControlInformation", securityControlInfo);

        return mapValues*/;
        return Map.of();
    }
}
