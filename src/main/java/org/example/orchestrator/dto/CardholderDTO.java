package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
public class CardholderDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6453296436707921907L;
    private CardholderNameDTO cardholderName;
    private List<IdentificationCardholderDTO> identification;
}
