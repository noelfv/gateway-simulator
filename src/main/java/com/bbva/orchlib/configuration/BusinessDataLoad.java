package com.bbva.orchlib.configuration;


import com.bbva.orchlib.featuretoggle.businessdata.BusinessData;
import com.bbva.orchlib.validations.ValidationsGlobal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;


/**
 * RulesL configuration class. Data
 */
@Configuration
@PropertySource("classpath:application-data.yml")
@ConfigurationProperties()
@Getter
@Setter
public class BusinessDataLoad {
    private BusinessData data;

    @PostConstruct
    public void businessData() {
        ValidationsGlobal.setBusinessData(data);
    }
}