package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class AddressDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -3874607103792631284L;

    private String townName;
    private String countrySubDivisionMajorName;
    private String country;
    private String postalCode;
}
