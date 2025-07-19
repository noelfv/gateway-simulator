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
public class ProcessingResultDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 3277729279632656626L;

	private String approvalCode;
	private ApprovalDataDTO approvalData;
	private List<ActionDTO> action;
	private ResultDataDTO resultData;
	private List<AdditionalInformationDTO> additionalInformation;
}
