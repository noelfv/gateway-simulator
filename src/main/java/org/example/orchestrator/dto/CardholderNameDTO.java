package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class CardholderNameDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7364891612616620931L;
    private String name;
}
