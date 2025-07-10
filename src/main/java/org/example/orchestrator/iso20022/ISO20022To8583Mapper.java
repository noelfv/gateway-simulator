package org.example.orchestrator.iso20022;


import org.apache.commons.lang3.StringUtils;
import org.example.orchestrator.ParserException;
import org.example.orchestrator.dto.*;
import org.example.orchestrator.iso8583.ISO8583;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ISO20022To8583Mapper {

    private ISO20022To8583Mapper() {
    }

    public static ISO8583 translateToISO8583(ISO20022 inputISO20022) throws ParserException {
        try {
            return ISO8583.builder()
                    .primaryAccountNumber(
                            /* mapping of primaryAccountNumber
                             * environment.card.pan
                             */
                            StringUtils.defaultIfEmpty(inputISO20022.getEnvironment().getCard().getPan(), "")
                    )
                    .processingCode(
                            /* mapping of processing code
                             * transaction.transactionType (posiciones 1-2),
                             * transaction.accountFrom.accountType (posiciones 3-4)
                             * transaction.accountTo.accountType (posiciones 5-6)
                             * */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getTransaction().getTransactionType() +
                                            inputISO20022.getTransaction().getAccountFrom().getAccountType() +
                                            inputISO20022.getTransaction().getAccountTo().getAccountType(), "")
                    )
                    .transactionAmount(
                            /* mapping of processing code
                             *  transaction.transactionAmounts.transactionAmount.amount
                             */
                            StringUtils.defaultIfEmpty(
                                    convertDoubleToString(inputISO20022.getTransaction().getTransactionAmounts().getTransactionAmount().getAmount(), 12), "")
                    )

                    .settlementAmount(
                            /* mapping of settlementAmount
                             * transaction.transactionAmounts.reconciliationAmount.amount
                             */
                            StringUtils.defaultIfEmpty(
                                    convertDoubleToString(
                                            inputISO20022.getTransaction().getTransactionAmounts().getReconciliationAmount().getAmount(),
                                            12), "")
                    )
                    .cardHolderBillingAmount(
                            /*mapping of cardHolderBillingAmount
                             * transaction.transactionAmounts.cardholderBillingAmount.amount
                             */
                            StringUtils.defaultIfEmpty(
                                    convertDoubleToString(
                                            inputISO20022.getTransaction().getTransactionAmounts().getCardholderBillingAmount().getAmount(),
                                            12), ""))
                    .transmissionDateTime(
                            /*mapping of transmissionDateTime
                             * transaction.TransactionIdentification.TransmissionDateTime
                             */
                            StringUtils.defaultIfEmpty(
                                    convertFormatDateTime(
                                    inputISO20022.getTransaction().getTransactionId().getTransmissionDateTime()), "")
                    )
                    .conversionRate(
                            /*mapping of conversionRate REVISAR
                             * transaction.transactionAmounts.cardholderBillingAmount.EffectiveExchangeRate
                             */
                            StringUtils.defaultIfEmpty(
                                    getConversionRate(
                                            inputISO20022.getTransaction().getTransactionAmounts().getCardholderBillingAmount().getEffectiveExchangeRate(),
                                            8), "")
                    )
                    .conversionRateSettlement(
                            StringUtils.defaultIfEmpty(
                                    convertDoubleToString(
                                            inputISO20022.getTransaction().getTransactionAmounts().getReconciliationAmount().getEffectiveExchangeRate(),
                                            8), "")

                    )
                    .systemTraceAuditNumber(
                            /*mapping of systemTraceAuditNumber
                             * transaction.transactionId.systemTraceAuditNumber
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getTransaction().getTransactionId().getSystemTraceAuditNumber(), "")
                    )
                    .localTransactionTime(
                            /*mapping of localTransactionTime
                             * transaction.transactionId.localTime
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getTransaction().getTransactionId().getLocalTime(), "")
                    )
                    .localTransactionDate(
                            /*mapping of localTransactionDate
                             * transaction.transactionId.localDate
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getTransaction().getTransactionId().getLocalDate(), "")
                    )
                    .dateExpiration(
                            /*mapping of dateExpiration
                             * environment.card.expirationDate
                             */
                            StringUtils.defaultIfEmpty(
                                    convertFormatExpiryDate(
                                            inputISO20022.getEnvironment().getCard().getExpiryDate()), ""))
                    .settlementDate(
                            /*mapping of settlementDate
                             * context.transactionContext.settlementService.settlementServiceDates.settlementDate
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getContext().getTransactionContext().getSettlementService()
                                            .getSettlementServiceDates().getSettlementDate(), "")
                    )
                    .dateConversion(
                            // ======== TEMPORAL FIELD ========
                            getSupplementaryData(inputISO20022.getSupplementaryData(), "dateConversion")
                    )
                    .captureDate(
                            /*mapping of captureDate
                             * context.transactionContext.captureDate
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getContext().getTransactionContext().getCaptureDate(), "")
                    )
                    .merchantType(
                            /*mapping of merchantType
                             * context.transactionContext.merchantCategoryCode
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getContext().getTransactionContext().getMerchantCategoryCode(), "")
                    )
                    /*MAPEO TEMPORAL**/
                    .acquirerCountryCode(
                            /*mapping of acquirerCountryCode
                             * environment.acquirer.countryCode
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getEnvironment().getAcquirer().getCountry(), "")
                    )
                    /*MAPEO TEMPORAL**/
                    // Resolve pointServiceEntryModeDTO
                    .pointServiceEntryMode(
                            /*mapping of pointServiceEntryMode
                             * context.PointOfServiceContext.CardDataEntryMode
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getContext().getPointOfServiceContext().getCardDataEntryMode(), "")
                    )
                    .cardSequenceNumber(
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getEnvironment().getCard().getCardSequenceNumber(), "")
                    )
                    .pointServiceConditionCode(
                            /*mapping of pointServiceConditionCode
                             * transaction.messageReason
                             */
                            StringUtils.defaultIfEmpty(inputISO20022.getTransaction().getMessageReason(), "")
                    )
                    .pointServicePIN(
                            // ======== TEMPORAL FIELD ========
                            getSupplementaryData(inputISO20022.getSupplementaryData(), "pointServicePIN")
                    )
                    .amountTransactionFee(
                            /*mapping of amountTransactionFee
                             * transaction.transactionAmounts.transactionFee.amount
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getContext().getTransactionContext().getReconciliation().getDate(), "")
                    )
                    .acquiringInstitutionIdentificationCode(
                            /*mapping of acquiringInstitutionIdentificationCode
                             * environment.acquirer.applicationCode
                             * environment.acquirer.id
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getEnvironment().getAcquirer().getId(), "")
                    )
                    .forwardingInstitutionIdentificationCode(
                            /*mapping of forwardingInstitutionIdentificationCode
                             * environment.issuer.applicationCode
                             * environment.issuer.id
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getEnvironment().getSender().getId(), "")
                    )
                    .trackTwoData(
                            /*mapping of trackTwoData
                             * environment.card.track2
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getEnvironment().getCard().getTrack2().getTextValue(), "")
                    )
                    .retrievalReferenceNumber(
                            /*mapping of retrievalReferenceNumber
                             * transaction.transactionId.retrievalReferenceNumber
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getTransaction().getTransactionId().getRetrievalReferenceNumber(), "")
                    )
                    .authorizationIdentificationResponse(
                            /*mapping of authorizationIdentificationResponse
                             * processingResult.approvalCode
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getProcessingResult().getApprovalCode(), "")
                                    //inputISO20022.getProcessingResult() != null ? inputISO20022.getProcessingResult().getApprovalCode() : "", "")
                    )
                    .responseCode(
                            /*mapping of responseCode
                             * processingResult.resultData.result
                             */
                            StringUtils.defaultIfEmpty(
                               inputISO20022.getProcessingResult().getResultData().getOtherResultDetails(), "")
                            //inputISO20022.getProcessingResult() != null ? inputISO20022.getProcessingResult().getResultData().getOtherResultDetails() : "", "")
                    )
                    .cardAcceptorTerminalIdentification(
                            /*mapping of cardAcceptorTerminalIdentification
                             * environment.terminal.terminalId.id
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getEnvironment().getTerminal().getTerminalId().getId(), "")
                    )
                    .cardAcceptorIdentificationCode(
                            /*mapping of cardAcceptorIdentificationCode
                             * environment.acceptor.id
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getEnvironment().getAcceptor().getId(), "")
                    )
                    .cardAcceptorNameLocation(
                            /*mapping of cardAcceptorNameLocation
                             * environment.acceptor.nameAndLocation
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getEnvironment().getAcceptor().getNameAndLocation(), "")
                    )
                    .additionalResponseData(
                            /*mapping of additionalResponseData
                             * Transaction.AdditionalData type and value
                             */
                            StringUtils.defaultIfEmpty(
                                    getAdditionalDataValue(inputISO20022.getTransaction().getAdditionalData(), "additionalResponseData"), "")
                    )
                    .trackOneData(
                            /*mapping of trackOneData
                             * environment.card.track1
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getEnvironment().getCard().getTrack1(), "")
                    )
                    .additionalDataRetailer(
                            /*mapping of additionalDataRetailer
                             * PENDIENTE
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getEnvironment().getSender().getAdditionalId().getValue(), "")
                    )
                    .transactionCurrencyCode(
                            /*mapping of transactionCurrencyCode
                             * transaction.transactionAmounts.transactionAmount.currency
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getTransaction().getTransactionAmounts()
                                            .getTransactionAmount().getCurrency(), "")
                    )
                    .settlementCurrencyCode(
                            /*mapping of settlementCurrencyCode
                             * transaction.transactionAmounts.reconciliationAmount.currency
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getTransaction().getTransactionAmounts()
                                            .getReconciliationAmount().getCurrency(), "")
                    )
                    .pinData(
                            /*mapping of pinData
                             * context.verification.verificationInformation.pinData*/

                            StringUtils.defaultIfEmpty(findPinData(inputISO20022), "")
                    )
                    .securityControlInformation(
                            /*mapping of additionalDataRetailer
                             * securityTrailer.macData.algorithm
                             * securityTrailer.macData.derivedInformation
                             * securityTrailer.macData.keyProtection
                             * securityTrailer.macData.keyIndex
                             * securityTrailer.macData.paddingMethod
                             * Validar como determinar si es Visa o Mastercard
                             */
                            StringUtils.defaultIfEmpty(
                                    getSecurityControlValue(inputISO20022.getSecurityTrailer().getMacData()), "")
                    )
                    .additionalAmounts(
                            /*mapping of additionalAmounts
                             * transaction.additionalAmounts.amount.amount
                             */
                            StringUtils.defaultIfEmpty(
                                    getAdditionalAmountSubFields(inputISO20022.getTransaction().getAdditionalData(), "accountType"), "") +
                                    StringUtils.defaultIfEmpty(
                                            getAdditionalAmountSubFields(inputISO20022.getTransaction().getAdditionalData(), "amountType"), "") +
                                    StringUtils.defaultIfEmpty(
                                            getAdditionalAmountSubFields(inputISO20022.getTransaction().getAdditionalData(), "currencyCode"), "") +
                                    StringUtils.defaultIfEmpty(
                                            getAdditionalAmountSubFields(inputISO20022.getTransaction().getAdditionalData(), "indicator"), "") +
                                    StringUtils.defaultIfEmpty(
                                    getAdditionalAmountValue(inputISO20022.getTransaction().getAdditionalAmount()), "")

                    )
                    .integratedCircuitCard(
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getIccRelatedData(), "")
                    )
                    .paymentAccountData(
                            // ======== TEMPORAL FIELD ========
                            getSupplementaryData(inputISO20022.getSupplementaryData(), "paymentAccountData")
                    )
                    .redemptionPoints(
                            /*mapping of redemptionPoints
                             * transaction.additionalData
                             */
                            StringUtils.defaultIfEmpty(
                                    getAdditionalDataValue(inputISO20022.getTransaction().getAdditionalData(),
                                            "redemptionPoints"), "")
                    )
                    .campaignData(
                            /*mapping of campaignData
                             * Se necesita validar, documentacion no especifica
                             */
                            StringUtils.defaultIfEmpty(
                                    getAdditionalDataValue(inputISO20022.getContext().getSaleContext().getAdditionalData(),
                                            "campaignData"), "")
                    )
                    .cardholderBillingCurrencyCode(
                            /*mapping of cardholderBillingCurrencyCode
                             * transaction.transactionAmounts.cardholderBillingAmount.currency
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getTransaction().getTransactionAmounts()
                                            .getCardholderBillingAmount().getCurrency(), "")
                    )
                    .posTerminalData(
                            /*mapping of posTerminalData
                             * body.environment.terminal.terminalId.assigner ** terminal.id ya en uso
                             */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getEnvironment().getTerminal().getTerminalId().getAssigner(), "")
                    )
                    .posCardIssuer(
                            /* mapping of posCardIssuer
                            * body.environment.issuer.shortname + id fill with zeros to position 19

                            mapValue(IsoField.POS_CARD_ISSUER,
                                    (inputISO20022.getEnvironment().getIssuer().getShortName() +
                                            inputISO20022.getEnvironment().getIssuer().getId() +
                                            "0000000000000000000").substring(0,20))
                            */
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getEnvironment().getIssuer().getAssigner(), "")

                    )
                    .postalCode(
                            /*mapping of postalCoda
                             * body.environment.acquirer.additionalData.value*/

                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getEnvironment().getAcquirer().getAdditionalId().getValue(), "")
                    )
                    .networkData(
                            StringUtils.defaultIfEmpty(
                                    getTraceDataValue(inputISO20022.getTraceData(), "posAdditionalData"), "")
                    )
                    .networkManagementInformationCode(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "networkManagementInfoCode"), "")
                    )
                    .serviceIndicator(
                            // ======== TEMPORAL FIELD ========
                            getSupplementaryData(inputISO20022.getSupplementaryData(), "serviceIndicator")
                    )
                    .messageSecurityCode(
                            // ======== TEMPORAL FIELD ========
                            getSupplementaryData(inputISO20022.getSupplementaryData(), "messageSecurityCode")
                    )
                    .originalDataElements(
                            StringUtils.defaultIfEmpty(
                                    getOriginalDataElementsValue(inputISO20022.getTransaction().getTransactionId().getOriginalDataElements()), "")
                    )
                    .accountIdentification(
                            StringUtils.defaultIfEmpty(
                                    inputISO20022.getTransaction().getAccountTo().getAccountId(), "")
                    )
                    .transactionData(
                            // ======== TEMPORAL FIELD ========
                            getSupplementaryData(inputISO20022.getSupplementaryData(), "transactionData")
                    )
                    .additionalDataNationalUse(
                            // ======== TEMPORAL FIELD ========
                            getSupplementaryData(inputISO20022.getSupplementaryData(), "additionalDataNationalUse")
                    )
                    .keyManagement(
                            getSupplementaryData(inputISO20022.getSupplementaryData(), "keyManagement")
                    )
                    .authorizingAgentIdCode(
                            // ======== TEMPORAL FIELD ========
                            getSupplementaryData(inputISO20022.getSupplementaryData(), "authorizingAgentIdCode")
                    )
                    .cryptographicServiceMessage(
                            StringUtils.defaultIfEmpty(
                                    findKeyIdValue(inputISO20022), "")
                    )
                    .settlementData(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(),
                                            "settlementData"), "")
                    )
                    .issuerTraceId(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "issuerTraceId"), "")
                    )
                    .amountCardholderBillingFee(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "amountCardholderBillingFee"), "")
                    )
                    .primaryAccountNumberCountryCode(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "primaryAccountNumberCountryCode"), "")
                    )
                    .forwardingInstitutionCountryCode(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "forwardingInstitutionCountryCode"), "")
                    )
                    .networkInternationalId(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "networkInternationalId"), "")
                    )
                    .authorizationIdResponseLength(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "authorizationIdResponseLength"), "")
                    )
                    .amountSettlementFee(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "amountSettlementFee"), "")
                    )
                    .amountTransactionProcessingFee(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "amountTransactionProcessingFee"), "")
                    )
                    .amountSettlementProcessingFee(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "amountSettlementProcessingFee"), "")
                    )
                    .primaryAccountNumberExtended(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "primaryAccountNumberExtended"), "")
                    )
                    .trackThreeData(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "trackThreeData"), "")
                    )
                    .expandedAdditionalAmounts(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "expandedAdditionalAmounts"), "")
                    )
                    .additionalDataNationalUse2(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "additionalDataNationalUse2"), "")
                    )
                    .messageAuthenticationCode(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "messageAuthenticationCode"), "")
                    )
                    .settlementCode(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "settlementCode"), "")
                    )
                    .extendedPaymentCode(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "extendedPaymentCode"), "")
                    )
                    .receivingInstitutionCountryCode(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "receivingInstitutionCountryCode"), "")
                    )
                    .settlementInstitutionCountryCode(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "settlementInstitutionCountryCode"), "")
                    )
                    .messageNumber(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "messageNumber"), "")
                    )
                    .messageNumberLast(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "messageNumberLast"), "")
                    )
                    .dateAction(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "dateAction"), "")
                    )
                    .creditsNumber(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "creditsNumber"), "")
                    )
                    .creditsReversalNumber(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "creditsReversalNumber"), "")
                    )
                    .debitsNumber(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "debitsNumber"), "")
                    )
                    .debitsReversalNumber(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "debitsReversalNumber"), "")
                    )
                    .transferNumber(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "transferNumber"), "")
                    )
                    .transferReversalNumber(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "transferReversalNumber"), "")
                    )
                    .inquiriesNumber(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "inquiriesNumber"), "")
                    )
                    .authorizationNumber(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "authorizationNumber"), "")
                    )
                    .creditsProcessingFeeAmount(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "creditsProcessingFeeAmount"), "")
                    )
                    .creditsTransactionFeeAmount(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "creditsTransactionFeeAmount"), "")
                    )
                    .debitsProcessingFeeAmount(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "debitsProcessingFeeAmount"), "")
                    )
                    .debitsTransactionFeeAmount(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "debitsTransactionFeeAmount"), "")
                    )
                    .creditsAmount(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "creditsAmount"), "")
                    )
                    .creditsReversalAmount(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "creditsReversalAmount"), "")
                    )
                    .debitsAmount(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "debitsAmount"), "")
                    )
                    .debitsReversalAmount(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "debitsReversalAmount"), "")
                    )
                    .issuerFileUpdateCode(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "issuerFileUpdateCode"), "")
                    )
                    .fileSecurityCode(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "fileSecurityCode"), "")
                    )
                    .responseIndicator(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "responseIndicator"), "")
                    )
                    .replacementAmounts(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "replacementAmounts"), "")
                    )
                    .amountNetSettlement(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "amountNetSettlement"), "")
                    )
                    .payee(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "payee"), "")
                    )
                    .settlementInstitutionIdentificationCode(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "settlementInstitutionIdentificationCode"), "")
                    )
                    .receivingInstitutionIdentificationCode(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "receivingInstitutionIdentificationCode"), "")
                    )
                    .fileName(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "fileName"), "")
                    )
                    .accountIdentification1(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "accountIdentification1"), "")
                    )
                    .fleetServiceData(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "fleetServiceData"), "")
                    )
                    .additionalTransactionReferenceData(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "additionalTransactionReferenceData"), "")
                    )
                    .isoUse(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "isoUse"), "")
                    )
                    .reservedNationalUse(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "reservedNationalUse"), "")
                    )
                    .reservedNationalUse2(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "reservedNationalUse2"), "")
                    )
                    .privateData(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "privateData"), "")
                    )
                    .messageAuthenticationCode2(
                            StringUtils.defaultIfEmpty(
                                    getSupplementaryData(inputISO20022.getSupplementaryData(), "messageAuthenticationCode2"), "")
                    )
                    .header(
                            StringUtils.defaultIfEmpty(
                                    getTraceDataValue(inputISO20022.getTraceData(), "header"), "")
                    )
                    .messageType(
                            StringUtils.defaultIfEmpty(
                                    // Cambio de tipo de mensaje interno al de la marca
                                    // RulesOrchestrator.getLRAValue(LRARuleType.OUTPUT, getNetworkName(), inputISO20022.getMessageFunction()))
                                   // inputISO20022.getMessageFunction(), "")
                                    inputISO20022.getAddendumData().getAdditionalData().get(1).getValue(),"")
                    )
                    .build();
        } catch (Exception e) {
            throw new ParserException("Error trying 20022 -> 8583: " + e.getMessage());
        }
    }


    private static String convertDoubleToString(Double value, int padding) {
        String format = "%0" + (padding + 1) + ".2f";
        return (value == null) ? "" : String.format(format, value);
    }

    private static String getConversionRate(Double value, int padding) {
        if (value == null) {
            return "";
        }

        StringBuilder conversionRate = new StringBuilder(value.toString());
        while (conversionRate.length() < padding) {
            conversionRate.append("0");
        }

        int dotPosition = conversionRate.indexOf(".");
        int precision = conversionRate.length() - (dotPosition + 1);
        conversionRate.deleteCharAt(dotPosition);
        conversionRate.insert(0, precision);
        return conversionRate.toString();
    }

    private static String getAdditionalDataValue(List<AdditionalDataDTO> list, String key) {
        AdditionalDataDTO listParam = list.stream().filter(p -> p.getKey().equals(key))
                .findFirst().orElse(null);
        return (listParam == null) ? "" : listParam.getValue();
    }

    private static String getAdditionalAmountValue(List<AdditionalAmountDTO> list) {
        AdditionalAmountDTO listParam = list.stream().filter(p -> p.getKey().equals("additionalAmounts"))
                .findFirst().orElse(null);
        return (listParam == null)
                ? ""
                : convertDoubleToString(
                listParam.getAmount().getAmount(),
                12
        ).replace(".","");
    }

        private static String getAdditionalAmountSubFields(List<AdditionalDataDTO> list, String subField){
            AdditionalDataDTO listParam = list.stream().filter(p -> p.getKey().equals(subField))
                    .findFirst().orElse(null);
            return (listParam == null) ? "" : listParam.getValue();
        }

    private static String getTraceDataValue(List<TraceDataDTO> list, String key) {
        TraceDataDTO listParam = list.stream().filter(p -> p.getKey().equals(key))
                .findFirst().orElse(null);
        return (listParam == null) ? "" : listParam.getValue();
    }

    private static String getSupplementaryData(List<SupplementaryDataDTO> list, String key) {
        SupplementaryDataDTO listParam = list.stream().filter(p -> p.getPlaceAndName().equals(key))
                .findFirst().orElse(null);
        return (listParam == null) ? "" : listParam.getEnvelope();
    }

    private static String getSecurityControlValue(MacDataDTO data) {
        return data.getKeyProtection()
                + data.getAlgorithm()
                + data.getDerivedInformation()
                + data.getKeyIndex();

    }

    private static String getOriginalDataElementsValue(OriginalDataElementsDTO data) {
        return data.getMessageFunction()
                + data.getSystemTraceAuditNumber()
                + data.getTransmissionDateTime()
                + data.getAcquirerId()
                + data.getSenderIdentification();
    }

    private static String findPinData(ISO20022 iso20022) {
        VerificationDTO verification = iso20022.getContext().getVerification().get(0);
        if (verification == null) {
            return "";
        }
        VerificationInformationDTO verificationInformation = verification.getVerificationInformation().get(0);
        if (verificationInformation == null) {
            return "";
        }
        String pinData = verificationInformation.getValue().getPinData().getEncryptedPINBlock();
        return (pinData == null) ? "" : pinData;
    }

    private static String findKeyIdValue(ISO20022 iso20022) {
        ProtectedDataDTO protectedDataDTO = iso20022.getProtectedData().get(0);
        if (protectedDataDTO == null) {
            return "";
        }
        RecipientDTO recipientDTO = protectedDataDTO.getEnvelopedData().getRecipient().get(0);
        if (recipientDTO == null) {
            return "";
        }
        String keyId = recipientDTO.getKek().getKekId().getKeyId();
        return (keyId == null) ? "" : keyId;
    }

    public static String getOriginalIsoMessage(ISO20022 iso20022) {
        try {
            return iso20022
                    .getAddendumData()
                    .getAdditionalData()
                    .stream()
                    .filter((dataDTO -> dataDTO.getKey().equals("ISO8583_HOST")))
                    .map(AdditionalDataDTO::getValue)
                    .findFirst()
                    .orElseThrow();
        } catch (Exception e) {
            throw new ParserException("Error trying 20022 -> 8583: " + e.getMessage());
        }
    }

    public static String convertFormatDateTime(String value) {
        if (value == null || value.isEmpty()) {
                return null;
        }

        try {
                // Parseando la fecha ISO con zona horaria Z (UTC)
                Instant instant = Instant.parse(value);
                ZonedDateTime zonedDateTime = instant.atZone(ZoneOffset.UTC);

                // Formateo al patrón MMddHHmmss
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMddHHmmss");

                return zonedDateTime.format(outputFormatter);
        } catch (Exception e) {
                return null;
        }
    }

    public static String convertFormatExpiryDate(String value) {
        if (value == null || value.isEmpty()) {
                return null;
        }
        try {
                // Parsear la fecha en formato yyyy-MM
                YearMonth ym = YearMonth.parse(value);

                // Formatear a yyMM
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMM");
                return ym.format(formatter);
        } catch (Exception e) {
                return null;
        }
    }

}
