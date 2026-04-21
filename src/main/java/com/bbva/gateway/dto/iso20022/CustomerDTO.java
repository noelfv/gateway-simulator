package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1782091267241782632L;

    private String customerIdentification;
    private String customerDesignation;
    private NameDTO name;
    private LocalDataDTO localData;
    private List<AdditionalDataDTO> additionalData;
}
