package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiverDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7364905320744172237L;
    private String id;
}
