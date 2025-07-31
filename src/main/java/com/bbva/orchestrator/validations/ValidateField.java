package com.bbva.orchestrator.validations;

import com.bbva.orchestrator.validations.enums.ConfigMessageFunctions;
import com.bbva.orchestrator.validations.enums.ConfigValidations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateField {
    private final String field;
    private String required;
    private final String messageFunction;
    private final String format;
    private final ConfigMessageFunctions[] messageFunctionRequired;


    public ValidateField(ConfigValidations configValidations, String field, String messageFunction) {
        this.field = (field == null || field.equals("null")) ? null : field;
        this.messageFunction = messageFunction;
        this.format = configValidations.getFormat();
        this.messageFunctionRequired = configValidations.getRequired();

    }

    public Map<String, Object> validField() {

        Optional<ConfigMessageFunctions> optionalMessageFunction = Arrays.stream(messageFunctionRequired).filter(m -> this.messageFunction.equals(m.getMessageFunction())).findFirst();
        if (optionalMessageFunction.isEmpty()) {
            if (field == null) {
                return this.formatResponse(true, "OK");
            }
            if (!field.isEmpty()) {
                return this.formatResponse(false, "Tipo de mensaje iso no valido");
            } else {
                return this.formatResponse(true, "OK");
            }
        }

        //Valid required field
        this.required = optionalMessageFunction.get().getFieldRequired();
        if (!this.isRequired()) {
            return this.formatResponse(false, "El campo es obligatorio");
        }

        //Valid format
        if ((this.field != null) && !this.field.isEmpty() && !this.validateFormat()) {
            return this.formatResponse(false, "El campo no cumple con el formato");
        }

        return this.formatResponse(true, "OK");
    }

    /**
     * Mapping response
     */
    private Map<String, Object> formatResponse(Boolean isValid, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("isValid", isValid);
        response.put("message", message);

        return response;
    }

    /**
     * Method that valid the mandatory field according
     */
    private boolean isRequired() {
        return this.field != null && (!this.field.isEmpty() || (!this.required.equals("REQUIRED")));
    }

    /**
     * Validate format according configuration
     */
    private boolean validateFormat() {
        boolean isValid;
        Pattern pattern = Pattern.compile(this.getPattern());
        Matcher matcher = pattern.matcher(this.field);
        isValid = matcher.matches();

        return isValid;
    }

    /**
     * Get pattern according format
     */
    private String getPattern() {
        String pattern = "";
        switch (this.format) {
            case "number":
                pattern = "([0-9]*[.])?[0-9]+";
                break;
            case "alphabetic":
                pattern = "[a-zA-Z]*";
                break;
            case "alphanumeric":
                pattern = "[a-zA-Z0-9]*";
                break;
            case "alphanumericSpecial":
                pattern = "[\\p{ASCII}]*";
                break;
            case "specialNumeric":
                pattern = "[\t\r\n\f\s0-9]{2,99}";
                break;
            default:
                break;
        }

        return pattern;
    }

}
