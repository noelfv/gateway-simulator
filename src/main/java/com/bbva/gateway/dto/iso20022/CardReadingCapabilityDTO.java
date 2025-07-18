package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class CardReadingCapabilityDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4873100928747911288L;
    private String capability;
}
