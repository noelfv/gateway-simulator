package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
public class ContextDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 8757346436707926907L;
	private TransactionContextDTO transactionContext;
	private List<VerificationDTO> verification;
	private PointOfServiceContextDTO pointOfServiceContext;
	private SaleContextDTO saleContext;
}
