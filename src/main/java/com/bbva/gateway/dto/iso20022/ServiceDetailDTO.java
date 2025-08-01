package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDetailDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6378298905616629568L;
    private String key;
    private String value;
}
