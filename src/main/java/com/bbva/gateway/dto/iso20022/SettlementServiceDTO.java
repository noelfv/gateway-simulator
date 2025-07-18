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
public class SettlementServiceDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -7395738749852969550L;
	private SettlementServiceDatesDTO settlementServiceDates;
	private List<AdditionalSettlementInformationDTO> additionalSettlementInformation;
 }
