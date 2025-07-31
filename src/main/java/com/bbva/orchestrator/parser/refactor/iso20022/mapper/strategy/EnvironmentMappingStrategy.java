package com.bbva.orchestrator.parser.refactor.iso20022.mapper.strategy;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.parser.common.ISOSubFieldProcess;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.refactor.iso20022.mapper.SectionMappingStrategy;
import com.bbva.orchestrator.parser.refactor.utils.FieldProcessingService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component // o @Named si usas CDI
public class EnvironmentMappingStrategy implements SectionMappingStrategy<EnvironmentDTO> {

    private final FieldProcessingService fieldService;

    public EnvironmentMappingStrategy(FieldProcessingService fieldService) {
        this.fieldService = fieldService;
    }

    @Override
    public EnvironmentDTO map(ISO8583 input, Map<String, String> subFields) {
        CardDTO card = CardDTO.builder()
                .pan(input.getPrimaryAccountNumber())
                .expiryDate(fieldService.convertFormatExpiryDate(input.getDateExpiration()))
                .cardSequenceNumber(input.getCardSequenceNumber())
                .track1(input.getTrackOneData())
                .track2(Track2DTO.builder().textValue(input.getTrackTwoData()).build())
                .build();

        TerminalIdDTO terminalId = TerminalIdDTO.builder()
                .id(input.getCardAcceptorTerminalIdentification())
                .assigner(input.getPosTerminalData())
                .build();

        // ... resto del mapeo
        String type = ISOSubFieldProcess.channelTPVIndicator(input, subFields, GrpcHeadersInfo.getNetwork());
        String otherType = type != null && type.length() > 4 ? type.substring(4) : null;
        String typeValue = type != null ? type.substring(0,4) : null;

        CapabilitiesDTO capabilities = CapabilitiesDTO.builder().build();
        TerminalDTO terminal = TerminalDTO.builder()
                .capabilities(capabilities)
                .terminalId(terminalId)
                //.key(fieldService.getChannelTPVIndicator(input, subFields)) //REVISAR
                .key(typeValue)
                .otherType(otherType)
                .build();

        AcquirerDTO acquirer = AcquirerDTO.builder()
                .id(input.getAcquiringInstitutionIdentificationCode())
                .country(input.getAcquirerCountryCode())
                .additionalId(AdditionalIdDTO.builder()
                        .key("postalCode")
                        .value(input.getPostalCode())
                        .build())
                .build();

        // ... y así con acceptor, sender, issuer

        return EnvironmentDTO.builder()
                .card(card)
                .terminal(terminal)
                .acquirer(acquirer)
                .acceptor(AcceptorDTO.builder()
                        .id(input.getCardAcceptorIdentificationCode())
                        .nameAndLocation(input.getCardAcceptorNameLocation())
                        .build())
                .issuer(IssuerDTO.builder()
                        .assigner(input.getPosCardIssuer())
                        .build())
                .build();
    }
}