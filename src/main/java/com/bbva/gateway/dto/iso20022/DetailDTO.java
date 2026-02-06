package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2615248859972563891L;

    private String name;
    private String value;
}
