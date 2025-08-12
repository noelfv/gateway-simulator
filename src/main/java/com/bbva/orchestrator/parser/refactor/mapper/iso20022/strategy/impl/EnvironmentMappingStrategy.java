package com.bbva.orchestrator.parser.refactor.mapper.iso20022.strategy.impl;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.parser.common.ISOSubFieldProcess;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.refactor.mapper.iso20022.strategy.SectionMappingStrategy;
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
    }


    public void translate(ISO20022 input, ISO8583.ISO8583Builder iso8583Builder) {

        EnvironmentDTO env = input.getEnvironment();
        CardDTO card = env.getCard();
        TerminalDTO terminal = env.getTerminal();
        AcquirerDTO acquirer = env.getAcquirer();
        SenderDTO sender=env.getSender();
        AcceptorDTO acceptor = env.getAcceptor();
        IssuerDTO issuer = env.getIssuer();

        iso8583Builder
                .primaryAccountNumber(fieldService.getFieldValue(card , CardDTO::getPan, DEFAULT_EMPTY_VALUE))
                .dateExpiration(fieldService.getFieldValue(card , CardDTO::getExpiryDate, DEFAULT_EMPTY_VALUE))//El formato de fecha debe ser MMYY pero regresa como YYYY-MM
                .cardSequenceNumber(fieldService.getFieldValue(card , CardDTO::getCardSequenceNumber, DEFAULT_EMPTY_VALUE))
                .trackOneData(fieldService.getFieldValue(card, CardDTO::getTrack1, DEFAULT_EMPTY_VALUE))
                .trackTwoData(fieldService.getFieldValue(card.getTrack2(), Track2DTO::getTextValue, DEFAULT_EMPTY_VALUE))
                //Terminal Information
                .cardAcceptorTerminalIdentification(fieldService.getFieldValue(terminal.getTerminalId(), TerminalIdDTO::getId, DEFAULT_EMPTY_VALUE))
                .posTerminalData(fieldService.getFieldValue(terminal.getTerminalId() ,TerminalIdDTO::getAssigner, DEFAULT_EMPTY_VALUE))
                //acquirer Information
                .acquiringInstitutionIdentificationCode(fieldService.getFieldValue(acquirer,AcquirerDTO::getId, DEFAULT_EMPTY_VALUE))
                .acquirerCountryCode(fieldService.getFieldValue(acquirer, AcquirerDTO::getCountry, DEFAULT_EMPTY_VALUE))
                //.postalCode(fieldService.getFieldValue(acquirer.getAdditionalId(), AdditionalIdDTO::getValue, DEFAULT_EMPTY_VALUE))//Aca se debe de validar los campos key/value -> .key("postalCode")
                .postalCode(fieldService.getAdditionalDataValue(acquirer.getAdditionalId(), "postalCode", DEFAULT_EMPTY_VALUE))
                //sender Information
                .forwardingInstitutionIdentificationCode(fieldService.getFieldValue(sender, SenderDTO::getId, DEFAULT_EMPTY_VALUE))
                .additionalDataRetailer(fieldService.getAdditionalDataValue(sender.getAdditionalId(), "additionalDataRetailer", DEFAULT_EMPTY_VALUE))
                //acceptor Information
                .cardAcceptorIdentificationCode(fieldService.getFieldValue(acceptor, AcceptorDTO::getId , DEFAULT_EMPTY_VALUE))
                .cardAcceptorNameLocation(fieldService.getFieldValue(acceptor , AcceptorDTO::getNameAndLocation, DEFAULT_EMPTY_VALUE))
                //raiz
                .posCardIssuer(fieldService.getFieldValue(issuer , IssuerDTO::getAssigner, DEFAULT_EMPTY_VALUE));

    }



}