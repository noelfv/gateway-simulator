package com.bbva.orchlib.validations;

import com.bbva.gateway.dto.iso20022.ISO20022;


public interface IValidationsLocalErr {
      ISO20022 validationsLocalErr(ISO20022 iso20022, String validationName);
}
