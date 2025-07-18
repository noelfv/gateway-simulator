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
public class AdditionalServiceDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4628901505616620394L;
    private String key;
    private String otherType;
    private List<ServiceDetailDTO> serviceDetail;
    private String otherResult;
}
