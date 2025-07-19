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
public class VerificationResultDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8634189062932156189L;
    private String key;
    private String entity;
    private String otherEntity;
    private String otherResult;
    private List<ResultDetailsDTO> resultDetails;
}
