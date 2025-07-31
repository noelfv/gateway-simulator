package com.bbva.orchestrator.parser.refactor.iso20022.mapper.strategy;


import com.bbva.gateway.dto.iso20022.*;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.refactor.iso20022.mapper.SectionMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ProtectedDataMappingStrategy implements SectionMappingStrategy<List<ProtectedDataDTO>> {

    @Override
    public List<ProtectedDataDTO> map(ISO8583 input, Map<String, String> subFields) {
        List<ProtectedDataDTO> protectedDataList = new ArrayList<>();

        String cryptographicServiceMessage = input.getCryptographicServiceMessage();
        if (cryptographicServiceMessage == null || cryptographicServiceMessage.isEmpty()) {
            return protectedDataList; // Retorna lista vacía si no hay datos
        }

        // Construcción del objeto ProtectedDataDTO
        KEKIdDTO kekId = KEKIdDTO.builder()
                .keyId(cryptographicServiceMessage)
                .build();

        KEKDTO kek = KEKDTO.builder()
                .kekId(kekId)
                .build();

        RecipientDTO recipient = RecipientDTO.builder()
                .kek(kek)
                .build();

        EnvelopedDataDTO envelopedData = EnvelopedDataDTO.builder()
                .recipient(List.of(recipient))
                .build();

        ProtectedDataDTO protectedData = ProtectedDataDTO.builder()
                .envelopedData(envelopedData)
                .build();

        protectedDataList.add(protectedData);

        return protectedDataList;
    }
}