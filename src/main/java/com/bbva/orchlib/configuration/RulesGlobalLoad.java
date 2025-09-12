package com.bbva.orchlib.configuration;


import com.bbva.orchlib.featuretoggle.rulesglobal.*;
import com.bbva.orchlib.rules.RulesCommon;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
@PropertySource("classpath:application-global.yml")
@ConfigurationProperties()
@Getter
@Setter
public class RulesGlobalLoad {

    private RulesGlobal global;

    @PostConstruct
    public void rulesGlobal() {

        loadRulesValidationsGlobal();
        RulesCommon.setFilterLabelsGlobal(global.getFilterLabels());
    }


    public void loadRulesValidationsGlobal() {
        RulesGlobalsListPreLoad rulesList = new RulesGlobalsListPreLoad();
        for (ValidationsGlobal validationsGlobal : global.getValidations()) {
            if (validationsGlobal.getRules() != null) {
                for (RulesG rule : validationsGlobal.getRules()) {
                    RulesGlobalsPreLoad rules = createRulesValidations(validationsGlobal, rule);
                    rulesList.getRulesList().add(rules);
                }
            }
        }

        RulesCommon.setRulesGlobalLoadList(rulesList);
    }


    private RulesGlobalsPreLoad createRulesValidations(ValidationsGlobal validations, RulesG rule) {
        RulesGlobalsPreLoad rules = new RulesGlobalsPreLoad();
        rules.setNetwork(validations.getNetwork());
        rules.setFunction(rule.getFunction());

        List<FilterGlobal> filtersGlobals = rule.getFilter();
        List<ConditionGlobal> allConditionGlobals = new ArrayList<>();

        for (FilterGlobal filterGlobal : filtersGlobals) {
            List<ConditionGlobal> condition = filterGlobal.getCondition();
            allConditionGlobals.addAll(condition);
        }

        rules.setCondition(allConditionGlobals);

        return rules;
    }
}
