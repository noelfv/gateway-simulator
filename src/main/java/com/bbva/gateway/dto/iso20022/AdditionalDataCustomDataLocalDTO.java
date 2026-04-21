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
public class AdditionalDataCustomDataLocalDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1826354641967117253L;
    private RequestDTO request;
    private ResponseDTO response;
}
