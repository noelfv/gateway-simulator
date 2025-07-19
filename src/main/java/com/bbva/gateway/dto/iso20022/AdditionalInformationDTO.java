package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalInformationDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8462540867530952914L;
    private String key;
    private String value;
}
