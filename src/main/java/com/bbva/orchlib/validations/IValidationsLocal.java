package com.bbva.orchlib.validations;

import com.bbva.gateway.dto.iso20022.ISO20022;

public interface IValidationsLocal {
    boolean validationsLocal(ISO20022 iso20022);
}
