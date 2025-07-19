package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KeyExchangeDataDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2937483612616628841L;
    private String keyChecksumValue;
    private String encryptedData;
    private String keySetIdentifier;
}
