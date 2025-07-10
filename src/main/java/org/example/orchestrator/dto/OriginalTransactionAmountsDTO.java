package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class OriginalTransactionAmountsDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3674122737037297221L;

    private TransactionAmountDTO transactionAmount;
    private CardholderBillingAmountDTO cardholderBillingAmount;
    private ReconciliationAmountDTO reconciliationAmount;
}
