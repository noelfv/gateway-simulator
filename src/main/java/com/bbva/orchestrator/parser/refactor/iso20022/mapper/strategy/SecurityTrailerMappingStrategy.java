package com.bbva.orchestrator.parser.refactor.iso20022.mapper.strategy;

import com.bbva.gateway.dto.iso20022.MacDataDTO;
import com.bbva.gateway.dto.iso20022.SecurityTrailerDTO;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.refactor.iso20022.mapper.SectionMappingStrategy;
import com.bbva.orchestrator.parser.refactor.utils.FieldProcessingService;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class SecurityTrailerMappingStrategy implements SectionMappingStrategy<SecurityTrailerDTO> {

    private final FieldProcessingService fieldService;

    public SecurityTrailerMappingStrategy(FieldProcessingService fieldService) {
        this.fieldService = fieldService;
    }

    @Override
    public SecurityTrailerDTO map(ISO8583 input, Map<String, String> subFields) {

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
                .keyIndex(fieldService.isNullOrEmptySubstring(secControlInformation, 6, secControlInformation != null ? secControlInformation.length() : 0))
                .build();

        return SecurityTrailerDTO.builder()
                .macData(macData)
                .build();
    }
}
