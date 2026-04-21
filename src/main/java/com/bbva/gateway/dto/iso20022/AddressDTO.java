package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -3874607103792631284L;

    private String addressLine1;
    private String addressLine2;
    private String streetName;
    private String buildingNumber;
    private String postalCode;
    private String townName;
    private String countrySubDivisionMinorName;
    private String countrySubDivisionMajorName;
    private String country;
}
