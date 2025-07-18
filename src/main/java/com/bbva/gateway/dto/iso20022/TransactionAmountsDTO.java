package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;


@Builder
@Getter
@Setter
public class TransactionAmountsDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 300490815015973537L;
	private TransactionAmountDTO transactionAmount;
	private ReconciliationAmountDTO reconciliationAmount;
	private CardholderBillingAmountDTO cardholderBillingAmount;
	private OriginalTransactionAmountsDTO originalTransactionAmounts;
}
