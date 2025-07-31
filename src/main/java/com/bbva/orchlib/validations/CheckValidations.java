package com.bbva.orchlib.validations;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchlib.enums.RuleType;
import com.bbva.orchlib.enums.ValidationRuleType;
import com.bbva.orchlib.featuretoggle.rulesglobal.RulesGlobalsListPreLoad;
import com.bbva.orchlib.featuretoggle.ruleslocal.RulesLocalsListPreLoad;
import com.bbva.orchlib.rules.RulesCommon;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.bbva.orchlib.rules.RulesValidations.executeValidationRule;


@Component
public class CheckValidations {


    IValidationsLocalErr validationsLocalErr;

    public CheckValidations(IValidationsLocalErr validationsLocalErr) {
        this.validationsLocalErr = validationsLocalErr;
    }


    /**
     * Check validations global boolean.
     *
     * @param iso20022map the iso 20022 map
     * @return the boolean
     */
    public boolean checkValidationsGlobal( ISO20022 iso20022map) {
        RulesGlobalsListPreLoad validationsGlobals = RulesCommon.filterRulesGlobalsListByNetwork(iso20022map.getNetworkName());
        validationsGlobals = RulesCommon.applyRuleGlobalCondition(iso20022map, validationsGlobals);

        List<String> validationRules = RulesCommon.getFunctionsRules(validationsGlobals);
        LogsTraces.writeInfo("[CheckValidations Global]: validations: " + validationRules.size());
        return processValidationRules(validationRules, new ValidationsGlobal(), iso20022map, ValidationRuleType.GLOBAL);
    }


    /**
     * Check validations local boolean.
     *
     * @param iso20022map      the iso 20022 map
     * @param objetoValidacion the objeto validacion
     * @return the boolean
     */
    public boolean checkValidationsLocal(ISO20022 iso20022map, Object objetoValidacion) {
        RulesLocalsListPreLoad validationsLocal = RulesCommon.filterRulesLocalsListByNetwork(iso20022map.getNetworkName(), RuleType.VALIDATIONS);
        validationsLocal = RulesCommon.applyRuleLocalCondition(iso20022map, validationsLocal);

        List<String> validationRules = RulesCommon.getFunctionsRules(validationsLocal);
        LogsTraces.writeInfo("[CheckValidations Local]: validations: " + validationRules.size());
        return processValidationRules(validationRules, objetoValidacion, iso20022map, ValidationRuleType.LOCAL);
    }


    public boolean processValidationRules(List<String> validationRules, Object objetoValidacion, ISO20022 iso20022map, ValidationRuleType validationRuleType) {

        for (String rule : validationRules) {
            if (!executeValidationRule(rule, objetoValidacion, iso20022map)) {
                LogsTraces.writeWarning("[CheckValidations Error]: Error in Validation " + rule);

                if(validationRuleType == ValidationRuleType.GLOBAL) {
                    ValidationsError.validationsErr(iso20022map, rule);
                }else{
                    validationsLocalErr.validationsLocalErr(iso20022map, rule);
                }

                return false;
            }
        }

        return true;
    }

}
