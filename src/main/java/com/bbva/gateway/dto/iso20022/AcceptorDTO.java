package com.bbva.gateway.dto.iso20022;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcceptorDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 7458559227954973155L;
	private String id;
	private String nameAndLocation;
	private LocalDataDTO localData;
	private List<AdditionalDataDTO> additionalData;
	private List<SponsoredMerchantDTO> sponsoredMerchant;
}
