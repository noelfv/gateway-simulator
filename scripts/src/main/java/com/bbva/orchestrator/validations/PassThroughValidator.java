package com.bbva.orchestrator.validations;

import java.util.Optional;

public class PassThroughValidator {

    private static final String LABEL = "pan";

    private PassThroughValidator() {
    }

    public static boolean isPassThroughEnabled(ISO20022 iso20022) {
        try {
            RulesLocalsListPreLoad rulesLocalsListByNetwork = RulesCommon.filterRulesLocalsListByNetwork(iso20022.getNetworkName(), RuleType.ORCHESTRATIONS);
            ConditionLocal passThroughCondition = null;
            for (RulesLocalsPreLoad rule : rulesLocalsListByNetwork.getRulesList()) {
                // Encontramos la regla con la condición del passThrough
                // Esta condición se encuentra definida en application-local
                Optional<ConditionLocal> conditionLocal = rule.getCondition()
                        .stream()
                        .filter(condition -> condition.getName().equals(LABEL))
                        .findFirst();

                if (conditionLocal.isPresent()) {
                    passThroughCondition = conditionLocal.get();
                    break;
                }
            }

            if (passThroughCondition == null) {
                return false;
            }

            String values = passThroughCondition.getValue();
            return !values.contains(passThroughCondition.getFilterLabelProcess());
        } catch (Exception e) {
            return false;
        }
    }
}
