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
public class ProcessingResultDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 3277729279632656626L;

	private String approvalCode;
	private ApprovalDataDTO approvalData;
	private List<ActionDTO> action;
	private ResultDataDTO resultData;
	private List<AdditionalInformationDTO> additionalInformation;
}
