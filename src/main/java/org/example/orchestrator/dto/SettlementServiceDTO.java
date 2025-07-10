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
public class SettlementServiceDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -7395738749852969550L;
	private SettlementServiceDatesDTO settlementServiceDates;
	private List<AdditionalSettlementInformationDTO> additionalSettlementInformation;
 }
