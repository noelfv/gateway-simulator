package com.bbva.orchestrator.core.mapper.iso20022.strategy.impl;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.orchestrator.core.dto.ISO8583;
import com.bbva.orchestrator.core.mapper.iso20022.strategy.SectionMappingStrategy;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AddendumDataMappingStrategy implements SectionMappingStrategy<AddendumDataDTO> {

    @Override
    public AddendumDataDTO mapper(ISO8583 input, Map<String, String> subFields) {

        CustomDataLocalDTO x;
        List<AdditionalDataDTO> additionalDataList = new ArrayList<>();
        //TODO Se debe considerar la siguiente logica, para los mensaje de entrada se debe de crear los
        // los objetos addendumData y customData con la siguiente logica:
        // 1. AddendumData:    MGSTYPE, ISO8583_HOST, ISO8583
        // 2. CustomDataLocal: MGSTYPE, ISO8583, FLOWTYPE(para el caso de flowType siempre seria ASYNC)
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("MSGTYPE")
                .value(input.getMessageType())
                .build());

        additionalDataList.add(AdditionalDataDTO.builder()
                .key("ISO8583_HOST")
                .value(input.getOriginalMessage())
                .build());

        additionalDataList.add(AdditionalDataDTO.builder()
                .key("ISO8583")
                .value(input.getPlainTextPCI())
                .build());

        // Mapeo campo 61
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("POS_DATA")
                .value(input.getPosCardIssuer())
                .build());

        //Campo 48 - Subcampo 15
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("Authorization_platform_advide_date_time")
                .value(subFields.getOrDefault("48.15.01","")+subFields.getOrDefault("48.15.02",""))
                .build());

        //Campo 48 - Subcampo 16
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("processor_pseudo_ICA")
                .value(subFields.getOrDefault("48.16",null))
                .build());

        //Campo 48 - Subcampo 17
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("authentication_indicator")
                .value(subFields.getOrDefault("48.17",null))
                .build());

        //Campo 48 - Subcampo 20
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("cardholder_verification_method")
                .value(subFields.getOrDefault("48.20",null))
                .build());

        //Campo 48 - Subcampo 21
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("acceptance_data")
                .value(subFields.getOrDefault("48.21",null))
                .build());

        //Campo 48 - Subcampo 22
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("multi_purpose_merchant_indicator")
                .value(subFields.getOrDefault("48.22",null))
                .build());

        //Campo 48 - Subcampo 23
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("payment_initiation_channel")
                .value(subFields.getOrDefault("48.23",null))
                .build());

        //Campo 48 - Subcampo 26
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("wallet_program_data")
                .value(subFields.getOrDefault("48.26",null))
                .build());

        //Campo 48 - Subcampo 27
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("additional_transaction_analysis")
                .value(subFields.getOrDefault("48.27",null))
                .build());

        //Campo 48 - Subcampo 30
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("token_transaction_identifier")
                .value(subFields.getOrDefault("48.30",null))
                .build());

        //Campo 48 - Subcampo 32
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("mastercard_assigned_ID")
                .value(subFields.getOrDefault("48.32",null))
                .build());

        //Campo 48 - Subcampo 34
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("dynamic_CVC_3_ATC_information")
                .value(subFields.getOrDefault("48.34",null))
                .build());

        //Campo 48 - Subcampo 35
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("contactless_non_card_form_factor_request_response")
                .value(subFields.getOrDefault("48.35",null))
                .build());

        //Campo 48 - Subcampo 37
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("additional_merchant_data")
                .value(subFields.getOrDefault("48.37",null))
                .build());

        //Campo 48 - Subcampo 49
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("time_validation_information")
                .value(subFields.getOrDefault("48.49",null))
                .build());

        //Campo 48 - Subcampo 51
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("merchant_on-behalf_services")
                .value(subFields.getOrDefault("48.51",null))
                .build());

        //Campo 48 - Subcampo 75
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("fraud_scoring_data")
                .value(subFields.getOrDefault("48.75",null))
                .build());

        //Campo 48 - Subcampo 76
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("mastercard_electronic_acceptance_indicator")
                .value(subFields.getOrDefault("48.76",null))
                .build());

        //Campo 48 - Subcampo 84
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("merchant_advice_code")
                .value(subFields.getOrDefault("48.84",null))
                .build());

        //Campo 48 - Subcampo 88
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("magnetic_stripe_compliance_status_indicator")
                .value(subFields.getOrDefault("48.88",null))
                .build());

        //Campo 48 - Subcampo 89
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("magnetic_stripe_compliance_error_indicator")
                .value(subFields.getOrDefault("48.89",null))
                .build());

        return AddendumDataDTO.builder()
                .additionalData(additionalDataList)
                .build();
    }

    @Override
    public Map<String, String> unMapper(String networkName,AddendumDataDTO input) {
        return Map.of();
    }
}