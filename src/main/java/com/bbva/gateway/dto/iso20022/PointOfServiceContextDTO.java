package com.bbva.gateway.dto.iso20022;

//import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
public class PointOfServiceContextDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 4039202634372560358L;
	private String cardDataEntryMode;
	private String otherCardDataEntryMode;
	private Boolean cardPresent;
	private Boolean cardholderPresent;
	private String unattendedLevelCategory;
	private Boolean partialApprovalSupported;
	private String otherSecurityCharacteristics;
	//@JsonProperty("eCommerceIndicator")
	private Boolean ecommerceIndicator;
	//@JsonProperty("mOTOCode")
	private String motoCode;
	private Boolean attendedIndicator;
	private List<AdditionalDataDTO> additionalData;
}