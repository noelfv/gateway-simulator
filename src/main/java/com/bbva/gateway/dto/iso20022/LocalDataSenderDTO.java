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
public class LocalDataSenderDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1836452095222093671L;
    private List<AdditionalDataDTO> additionalData;
}
