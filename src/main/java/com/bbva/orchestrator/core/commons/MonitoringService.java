package com.bbva.orchestrator.core.commons;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.orchestrator.configuration.ApplicationDataCache;
import com.bbva.orchestrator.core.dto.ISO8583;
import com.bbva.orchestrator.core.enums.ChannelOperator;
import com.bbva.orchestrator.core.enums.FilterOperator;
import com.bbva.orchestrator.core.enums.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
public class MonitoringService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringService.class);
    private static final List<String> MTI_INPUT = List.of("0100", "0120", "0400", "0420");
    private static final List<String> MTI_OUTPUT = List.of("0110", "0130", "0410", "0430");
    private static final String BIN_ADQUIRENTE_P2P = "420829";
    private static final String APPROVED = "Approved";
    private static final String DENIED = "Denied";
    private final ApplicationDataCache applicationDataCache;


    public MonitoringService(ApplicationDataCache applicationDataCache) {
        this.applicationDataCache = applicationDataCache;
    }

    public MonitoringDTO build(ISO8583 input, TransactionDTO transaction,EnvironmentDTO environment,ContextDTO context) {

        MonitoringDTO monitoring = MonitoringDTO.builder().build();
        String networkName = input.getNetworkName();
        String envAcceptorNameAndLocation = input.getCardAcceptorNameLocation();
        String envAcquirerId = input.getAcquiringInstitutionIdentificationCode();
        String envAcceptorId = input.getCardAcceptorIdentificationCode();
        String merchantCategory = input.getMerchantType();
        try {

            if (MTI_INPUT.contains(input.getMessageType())) {
                String binCode = extractBinCode(input.getPrimaryAccountNumber());
                String merchantName = extractMerchantName(envAcceptorNameAndLocation);
                long endDate = Instant.now().toEpochMilli();
                String currentTime = String.valueOf(endDate);
                monitoring.setStartDateMs(currentTime);
                monitoring.setBinCode(binCode);
                monitoring.setBinDescription(applicationDataCache.getBinDescription(networkName, binCode));
                monitoring.setMerchantNameAceptor(merchantName.trim());
                monitoring.setMerchantCategoryDescription(applicationDataCache.getCustomValue("merchant_type", merchantCategory));
                monitoring.setChannelFilter(channelFilterDescription(context.getPointOfServiceContext().getEcommerceIndicator(), environment.getTerminal().getKey()));
                monitoring.setOperationFilter(FilterOperator.getFilterOperator(transaction.getTransactionType()));
                monitoring.setTransactionTypeDescription(TransactionType.getTransactionType(transaction.getTransactionType()));
                monitoring.setCountryDate(getCurrentDatePeru());

                if (BIN_ADQUIRENTE_P2P.equals(envAcquirerId)) {
                    monitoring.setP2pType(envAcceptorNameAndLocation.substring(0, 4));
                    monitoring.setOriginBankCode(envAcceptorId);
                    monitoring.setOriginBankDescription(applicationDataCache.getCustomValue("bank_p2p", envAcceptorId));
                }
            } else {
                if (MTI_OUTPUT.contains(input.getMessageType())) {
                    monitoring.setTransactionStatus("00".equals(input.getResponseCode()) ? APPROVED : DENIED);
                } else {
                    LOGGER.info("No requiere generar bloque monitoreo messageType= {}", input.getMessageType());
                    return monitoring;
                }
            }
            return monitoring;
        } catch (RuntimeException e) {
            LOGGER.error("Error creating monitoring: {} ", e.getMessage());
            return monitoring;
        }
    }


    private  String extractBinCode(String binCode) {
        String resultBinCode = "";
        try {
            resultBinCode = binCode.substring(0, 6);
        } catch (Exception e) {
            LOGGER.info("Error getting the binCode from the field PAN {} - {}  ", binCode, e.getMessage());
        }
        return resultBinCode;
    }

    private static String extractMerchantName(String nameLocation) {
        String resultNameLocation = "";
        try {
            resultNameLocation = nameLocation.substring(0, 22);
        } catch (Exception e) {
            LOGGER.info("Error getting AcceptorName {} - {} ", nameLocation, e.getMessage());
        }
        return resultNameLocation;
    }

    public static String channelFilterDescription(Boolean eCommerceIndicator, String terminalKey) {
        if (Boolean.TRUE.equals(eCommerceIndicator)) {
            return "ECOMMER";
        }
        String channel = ChannelOperator.getChannelOperator(terminalKey);
        return Optional.ofNullable(channel).orElse("OTHER");
    }

    public static String getCurrentDatePeru() {

        ZonedDateTime horaActualPeru = ZonedDateTime.now(ZoneId.of("America/Lima"));
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        return horaActualPeru.format(formateador)+"-05:00";
    }
}
