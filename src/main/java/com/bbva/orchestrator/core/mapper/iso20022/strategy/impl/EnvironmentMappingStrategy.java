package com.bbva.orchestrator.core.mapper.iso20022.strategy.impl;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.commons.ISOSubFieldProcess;
import com.bbva.orchestrator.core.exception.MapperLocalException;
import com.bbva.orchestrator.core.mapper.iso20022.strategy.SectionMappingStrategy;
import com.bbva.orchestrator.core.utils.FieldProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EnvironmentMappingStrategy implements SectionMappingStrategy<EnvironmentDTO> {

    private final FieldProcessingService fieldService;

    @Override
    public EnvironmentDTO mapper(ISO8583 input, Map<String, String> subFields) {

        try {
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
            String typeValue = type != null ? type.substring(0, 4) : null;

            //CapabilitiesDTO capabilities = CapabilitiesDTO.builder().build();
            CapabilitiesDTO capabilities = null;

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


            // ======== FIELD 48 (ADDITIONAL DATA RETAILER) ========
            AdditionalIdDTO additionalDataRetailer = AdditionalIdDTO.builder()
                    .key("additionalDataRetailer")
                    .value(input.getAdditionalDataRetailer())
                    .build();

            SenderDTO sender = SenderDTO.builder()
                    // ======== FIELD 33 (FORWARDING INSTITUTION IDENTIFICATION CODE) ========
                    .id(input.getForwardingInstitutionIdentificationCode())
                    .additionalId(additionalDataRetailer)
                    .build();

            AcceptorDTO acceptor = AcceptorDTO.builder()
                    // ======== FIELD 42 (CARD ACCEPTOR IDENTIFICATION CODE) ========
                    .id(input.getCardAcceptorIdentificationCode())
                    // ======== FIELD 43 (CARD ACCEPTOR NAME AND LOCATION) ========
                    .nameAndLocation(input.getCardAcceptorNameLocation())
                    .build();

            IssuerDTO issuer = IssuerDTO.builder()
                    // ======== FIELD 61 (POS CARD ISSUER / OTHER AMOUNTS) ========
                    .assigner(input.getPosCardIssuer())
                    .build();

            return EnvironmentDTO.builder()
                    .card(card)
                    .terminal(terminal)
                    .acquirer(acquirer)
                    .sender(sender)
                    .acceptor(acceptor)
                    .issuer(issuer)
                    .build();
        } catch (RuntimeException e) {
            // Manejo de excepciones, puedes lanzar una RuntimeException o una excepción personalizada
            throw new MapperLocalException("PGWP-00121", "Error al mapear EnvironmentDTO desde ISO8583", e);
        }
    }

    @Override
    public Map<String, String> unMapper(EnvironmentDTO env) {
        Map<String, String> resultMap = new HashMap<>();

        CardDTO card = env.getCard();
        TerminalDTO terminal = env.getTerminal();
        AcquirerDTO acquirer = env.getAcquirer();
        SenderDTO sender = env.getSender();
        AcceptorDTO acceptor = env.getAcceptor();
        IssuerDTO issuer = env.getIssuer();


        // Card Information
        resultMap.put("primaryAccountNumber", fieldService.getFieldValue(card, CardDTO::getPan, DEFAULT_EMPTY_VALUE));
        resultMap.put("dateExpiration", fieldService.getFieldValue(card, CardDTO::getExpiryDate, DEFAULT_EMPTY_VALUE)); //El formato de fecha debe ser MMYY pero regresa como YYYY-MM
        resultMap.put("cardSequenceNumber", fieldService.getFieldValue(card, CardDTO::getCardSequenceNumber, DEFAULT_EMPTY_VALUE));
        resultMap.put("trackOneData", fieldService.getFieldValue(card, CardDTO::getTrack1, DEFAULT_EMPTY_VALUE));
        resultMap.put("trackTwoData", fieldService.getFieldValue(card.getTrack2(), Track2DTO::getTextValue, DEFAULT_EMPTY_VALUE));

        // Terminal Information
        resultMap.put("cardAcceptorTerminalIdentification", fieldService.getFieldValue(terminal.getTerminalId(), TerminalIdDTO::getId, DEFAULT_EMPTY_VALUE));
        resultMap.put("posTerminalData", fieldService.getFieldValue(terminal.getTerminalId(), TerminalIdDTO::getAssigner, DEFAULT_EMPTY_VALUE));

        // Acquirer Information
        resultMap.put("acquiringInstitutionIdentificationCode", fieldService.getFieldValue(acquirer, AcquirerDTO::getId, DEFAULT_EMPTY_VALUE));
        resultMap.put("acquirerCountryCode", fieldService.getFieldValue(acquirer, AcquirerDTO::getCountry, DEFAULT_EMPTY_VALUE));
        resultMap.put("postalCode", fieldService.getAdditionalDataValue(acquirer.getAdditionalId(), "postalCode", DEFAULT_EMPTY_VALUE));

        // Sender Information
        resultMap.put("forwardingInstitutionIdentificationCode", fieldService.getFieldValue(sender, SenderDTO::getId, DEFAULT_EMPTY_VALUE));
        resultMap.put("additionalDataRetailer", fieldService.getAdditionalDataValue(sender.getAdditionalId(), "additionalDataRetailer", DEFAULT_EMPTY_VALUE));

        // Acceptor Information
        resultMap.put("cardAcceptorIdentificationCode", fieldService.getFieldValue(acceptor, AcceptorDTO::getId, DEFAULT_EMPTY_VALUE));
        resultMap.put("cardAcceptorNameLocation", fieldService.getFieldValue(acceptor, AcceptorDTO::getNameAndLocation, DEFAULT_EMPTY_VALUE));

        // Issuer Information
        resultMap.put("posCardIssuer", fieldService.getFieldValue(issuer, IssuerDTO::getAssigner, DEFAULT_EMPTY_VALUE));

        return resultMap;
    }
}