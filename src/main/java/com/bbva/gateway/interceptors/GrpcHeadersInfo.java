package com.bbva.gateway.interceptors;

import lombok.Data;


public class GrpcHeadersInfo {

    private String traceId;

    public static String getTraceId(){
        return "traceID";
    }

    public static String getNetwork(){
        return "PEER02";
    }

    public static String getPort(){
        return "6034";
    }


}
