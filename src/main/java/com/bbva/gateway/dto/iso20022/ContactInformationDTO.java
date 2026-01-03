package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactInformationDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1782096436217919801L;
    private String personalEmailAddress;
}
