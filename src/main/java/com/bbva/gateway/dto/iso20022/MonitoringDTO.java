package com.bbva.gateway.dto.iso20022;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class MonitoringDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3782190745222287392L;
    private String startDateMs;
    private String endDateMs;
    private String differenceDateTime;
    private String binCode;
    private String binDescription;
    private String merchantNameAceptor;
    private String merchantCategoryDescription;
    private String transactionStatus;
    private String channelFilter;
    private String operationFilter;
    private String transactionTypeDescription;
    private String p2pType;
    private String originBankCode;
    private String originBankDescription;
}
