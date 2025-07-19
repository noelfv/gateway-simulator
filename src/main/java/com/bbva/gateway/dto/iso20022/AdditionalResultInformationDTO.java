package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalResultInformationDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4756290859975173841L;
    private String key;
    private String value;
}
