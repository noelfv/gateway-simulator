package com.bbva.orchlib.configuration;


import com.bbva.orchlib.featuretoggle.ruleslocal.*;
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
@PropertySource("classpath:application-local.yml")
@ConfigurationProperties()
@Getter
@Setter
public class RulesLocalLoad {

    private RulesLocal local;

    @PostConstruct
    public void rulesLocal() {
        loadRulesOrchestrations();
        loadRulesValidationsLocal();
        RulesCommon.setFilterLabelsLocal(local.getFilterLabels());

    }

    public void loadRulesOrchestrations() {
        RulesLocalsListPreLoad rulesList = new RulesLocalsListPreLoad();
        for (Orchestrations orchestration : local.getOrchestrations()) {
            if (orchestration.getRules() != null) {
                for (RulesL rule : orchestration.getRules()) {
                    RulesLocalsPreLoad rules = createRulesOrchestrations(orchestration, rule);
                    rulesList.getRulesList().add(rules);
                }
            }
        }
        RulesCommon.setRulesOrchLocalLoadList(rulesList);
    }


    public void loadRulesValidationsLocal() {
        RulesLocalsListPreLoad rulesList = new RulesLocalsListPreLoad();
        for (ValidationsLocal validationsLocal : local.getValidations()) {
            if (validationsLocal.getRules() != null) {
                for (RulesL rule : validationsLocal.getRules()) {
                    RulesLocalsPreLoad rules = createRulesValidations(validationsLocal, rule);
                    rulesList.getRulesList().add(rules);
                }
            }
        }

        RulesCommon.setRulesValidationsLocalLoadList(rulesList);
    }


    private RulesLocalsPreLoad createRulesOrchestrations(Orchestrations orchestration, RulesL rule) {
        RulesLocalsPreLoad rules = new RulesLocalsPreLoad();
        rules.setNetwork(orchestration.getNetwork());
        rules.setFunction(rule.getFunction());

        List<FilterLocal> filterLocals = rule.getFilter();
        List<ConditionLocal> allConditionLocals = new ArrayList<>();

        for (FilterLocal filterLocal : filterLocals) {
            List<ConditionLocal> conditionLocals = filterLocal.getCondition();
            allConditionLocals.addAll(conditionLocals);
        }

        rules.setCondition(allConditionLocals);

        return rules;
    }


    private RulesLocalsPreLoad createRulesValidations(ValidationsLocal validationsLocal, RulesL rule) {
        RulesLocalsPreLoad rules = new RulesLocalsPreLoad();
        rules.setNetwork(validationsLocal.getNetwork());
        rules.setFunction(rule.getFunction());

        List<FilterLocal> filterLocals = rule.getFilter();
        List<ConditionLocal> allConditionLocals = new ArrayList<>();

        for (FilterLocal filterLocal : filterLocals) {
            List<ConditionLocal> conditionLocals = filterLocal.getCondition();
            allConditionLocals.addAll(conditionLocals);
        }

        rules.setCondition(allConditionLocals);

        return rules;
    }
}
