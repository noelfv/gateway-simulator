package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContextDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 8757346436707926907L;
	private TransactionContextDTO transactionContext;
	private List<VerificationDTO> verification;
	private PointOfServiceContextDTO pointOfServiceContext;
	private SaleContextDTO saleContext;
}
