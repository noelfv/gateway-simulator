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
public class ResultDataDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -3221302859975603885L;
	private String result;
	private String resultDetails;
	private String otherResult;
	private String otherResultDetails;
	private List<AdditionalResultInformationDTO> additionalResultInformation;
}
