package org.example.orchestrator.validations;


import org.example.orchestrator.dto.ISO20022;

public class ValidationsLocal  {

    public static final String REQUIRED = "REQUIRED";
    public static final String OPTIONAL = "OPTIONAL";
    public static final String NUMBER = "number";
    public static final String ALPHA_NUMERIC = "alphanumeric";
    public static final String ALPHA_NUMERIC_SPECIAL = "alphanumericSpecial";


    public boolean validationsLocal(ISO20022 iso20022) {
        return true;
    }

    public boolean validateFieldsISO(ISO20022 iso20022) {
        iso20022.getMessageFunction();
        return true;
    }

}