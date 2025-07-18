package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Track2DTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2845377881979354333L;
    private String textValue;
}
