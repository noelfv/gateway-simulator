package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RiskAssessmentDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -3725110062232356293L;
    private String result;
}
