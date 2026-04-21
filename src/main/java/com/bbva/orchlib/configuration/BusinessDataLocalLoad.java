package com.bbva.orchlib.configuration;

import com.bbva.orchlib.configuration.preloaddto.businessdatalocal.BusinessDataLocal;
import com.bbva.orchlib.utils.BusinessDataLocalUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@PropertySource("classpath:application-datalocal.yml")
@ConfigurationProperties()
@Getter
@Setter
public class BusinessDataLocalLoad {

    private List<BusinessDataLocal> datalocal;

    @PostConstruct
    public void businessDatalocal() {
        BusinessDataLocalUtils.setBusinessDataLocalList(datalocal);
    }
}
