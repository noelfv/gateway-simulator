package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Builder
@Getter
@Setter
public class TransactionDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 6086764505616628896L;
	private TransactionIdDTO transactionId;
	private String transactionType;
	private AccountFromDTO accountFrom;
	private AccountToDTO accountTo;
	private TransactionAmountsDTO transactionAmounts;
	private Long transactionAttribute;
	private String messageReason;
	private List<OriginalAdditionalFeesDTO> originalAdditionalFee;
	private List<AdditionalFeesDTO> additionalFee;
	private List<AdditionalAmountDTO> additionalAmount;
	private List<AdditionalDataDTO> additionalData;
	private List<AdditionalServiceDTO> additionalService;
	private String networkManagementType;
	private KeyExchangeDataDTO keyExchangeData;
	private DetailedRequestedAmountDTO detailedRequestedAmount;
	private String otherTransactionAttribute;
	private List<String> alternateMessageReason;
}
