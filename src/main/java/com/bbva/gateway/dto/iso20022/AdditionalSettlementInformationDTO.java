package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalSettlementInformationDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6549012739852961201L;
    private String key;
    private String value;
}
