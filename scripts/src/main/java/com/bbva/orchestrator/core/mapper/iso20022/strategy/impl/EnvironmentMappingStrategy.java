package com.bbva.orchestrator.core.mapper.iso20022.strategy.impl;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.core.commons.ISOSubFieldProcess;
import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.exception.MapperFieldsException;
import com.bbva.orchestrator.core.mapper.iso20022.strategy.SectionMappingStrategy;
import com.bbva.orchestrator.core.utils.MapperUtil;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class EnvironmentMappingStrategy implements SectionMappingStrategy<EnvironmentDTO> {

    private final MapperUtil mapperUtil;
    
    public EnvironmentMappingStrategy(MapperUtil mapperUtil) {
        this.mapperUtil = mapperUtil;
    }
    

    @Override
    public EnvironmentDTO mapper(ISO8583 input, Map<String, String> subFields) {

        try {

            // ======== FIELD 35 (TRACK 2 DATA) ========
            Track2DTO track2 = Track2DTO
                    .builder()
                    .textValue(input.getTrackTwoData())
                    .build();

            CardDTO card = CardDTO.builder()
                    // ======== FIELD 23 (CARD SEQUENCE NUMBER) ========
                    .cardSequenceNumber(input.getCardSequenceNumber())
                    // ======== FIELD 14 (EXPIRATION DATE) ========
                    .expiryDate(mapperUtil.convertFormatExpiryDate(input.getDateExpiration()))
                    // ======== FIELD 2 (PAN) ========
                    .pan(input.getPrimaryAccountNumber())
                    // ======== FIELD 40 (SERVICE RESTRICTION CODE) ========
                    .serviceCode(input.getServiceRestrictionCode())
                    // ======== FIELD 45 (TRACK 1 DATA) ========
                    .track1(input.getTrackOneData())
                    .track2(track2)
                    .build();


            CapabilitiesDTO capabilities = CapabilitiesDTO.builder()
                    .approvalCodeLength(null)
                    .build();

            TerminalIdDTO terminalId = TerminalIdDTO.builder()
                    // ======== FIELD 41 (CARD ACCEPTOR TERMINAL IDENTIFICATION) ========
                    .id(input.getCardAcceptorTerminalIdentification())
                    // ======== FIELD 60 (POS TERMINAL DATA) ========
                    .assigner(input.getPosTerminalData())
                    .build();

            // ... resto del mapeo
            String type = ISOSubFieldProcess.channelTPVIndicator(input, subFields, GrpcHeadersInfo.getNetwork());
            String otherType = type != null && type.length() > 4 ? type.substring(4) : null;
            String typeValue = type != null ? type.substring(0, 4) : null;

            TerminalDTO terminal = TerminalDTO.builder()
                    .capabilities(capabilities)
                    .terminalId(terminalId)
                    //.key(mapperUtil.getChannelTPVIndicator(input, subFields)) //REVISAR
                    .key(typeValue)
                    .otherType(otherType)
                    .build();

            // ======== FIELD 62 (MAPPED AS POSTAL CODE) ========
            AdditionalIdDTO postalCodeData = AdditionalIdDTO.builder()
                    .key("postalCode")
                    .value(input.getPostalCode())
                    .build();

            AcquirerDTO acquirer = AcquirerDTO.builder()
                    // ======== FIELD 32 (ACQUIRING INSTITUTION IDENTIFICATION CODE) ========
                    .id(input.getAcquiringInstitutionIdentificationCode())
                    .additionalId(postalCodeData)
                    // ======== FIELD 19 (ACQUIRING INSTITUTION COUNTRY CODE) ========
                    .country(input.getAcquirerCountryCode())
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
            throw new MapperFieldsException("PGWP-00121", "Error al mapear EnvironmentDTO desde ISO8583", e);
        }
    }

    @Override
    public Map<String, String> unMapper(String networkName,EnvironmentDTO env) {
        Map<String, String> resultMap = new HashMap<>();

        CardDTO card = env.getCard();
        TerminalDTO terminal = env.getTerminal();
        AcquirerDTO acquirer = env.getAcquirer();
        SenderDTO sender = env.getSender();
        AcceptorDTO acceptor = env.getAcceptor();
        IssuerDTO issuer = env.getIssuer();


        // Card Information
        resultMap.put("primaryAccountNumber", mapperUtil.getFieldValue(card, CardDTO::getPan, DEFAULT_EMPTY_VALUE));
        resultMap.put("dateExpiration", mapperUtil.reConvertFormatExpiryDate(card.getExpiryDate())); //El formato de fecha debe ser MMYY pero regresa como YYYY-MM
        resultMap.put("cardSequenceNumber", mapperUtil.getFieldValue(card, CardDTO::getCardSequenceNumber, DEFAULT_EMPTY_VALUE));
        resultMap.put("trackOneData", mapperUtil.getFieldValue(card, CardDTO::getTrack1, DEFAULT_EMPTY_VALUE));
        resultMap.put("trackTwoData", mapperUtil.getFieldValue(card.getTrack2(), Track2DTO::getTextValue, DEFAULT_EMPTY_VALUE));

        // Terminal Information
        resultMap.put("cardAcceptorTerminalIdentification", mapperUtil.getFieldValue(terminal.getTerminalId(), TerminalIdDTO::getId, DEFAULT_EMPTY_VALUE));
        resultMap.put("posTerminalData", mapperUtil.getFieldValue(terminal.getTerminalId(), TerminalIdDTO::getAssigner, DEFAULT_EMPTY_VALUE));

        // Acquirer Information
        resultMap.put("acquiringInstitutionIdentificationCode", mapperUtil.getFieldValue(acquirer, AcquirerDTO::getId, DEFAULT_EMPTY_VALUE));
        resultMap.put("acquirerCountryCode", mapperUtil.getFieldValue(acquirer, AcquirerDTO::getCountry, DEFAULT_EMPTY_VALUE));
        resultMap.put("postalCode", mapperUtil.getAdditionalDataValue(acquirer.getAdditionalId(), "postalCode", DEFAULT_EMPTY_VALUE));

        // Sender Information
        // ======== FIELD 33 (FORWARDING INSTITUTION IDENTIFICATION CODE) ========
        resultMap.put("forwardingInstitutionIdentificationCode", mapperUtil.getFieldValue(sender, SenderDTO::getId, DEFAULT_EMPTY_VALUE));
        // ======== FIELD 48 (ADDITIONAL DATA RETAILER) ========
        resultMap.put("additionalDataRetailer", mapperUtil.getAdditionalDataValue(sender.getAdditionalId(), "additionalDataRetailer", DEFAULT_EMPTY_VALUE));

        // Acceptor Information
        resultMap.put("cardAcceptorIdentificationCode", mapperUtil.getFieldValue(acceptor, AcceptorDTO::getId, DEFAULT_EMPTY_VALUE));
        resultMap.put("cardAcceptorNameLocation", mapperUtil.getFieldValue(acceptor, AcceptorDTO::getNameAndLocation, DEFAULT_EMPTY_VALUE));

        // Issuer Information
        resultMap.put("posCardIssuer", mapperUtil.getFieldValue(issuer, IssuerDTO::getAssigner, DEFAULT_EMPTY_VALUE));

        return resultMap;
    }
}