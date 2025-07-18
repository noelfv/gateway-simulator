package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardholderDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6453296436707921907L;
    private CardholderNameDTO cardholderName;
    private List<IdentificationCardholderDTO> identification;
}
