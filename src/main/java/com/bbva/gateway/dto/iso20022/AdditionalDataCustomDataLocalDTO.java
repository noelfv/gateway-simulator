package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalDataCustomDataLocalDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1826354641967117253L;
    private RequestDTO request;
    private ResponseDTO response;
}
