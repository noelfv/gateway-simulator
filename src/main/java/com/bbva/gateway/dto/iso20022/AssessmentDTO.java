package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5748374029054139832L;
    private String itemNumber;
    private String id;
    private String providerIdentification;
}
