package org.example.orchestrator.common;

public interface ISOField {
    int getId();
    String getName();
    ISODataType getTypeData();
    boolean isVariable();
    int getLength();
    String parseISOValue(ISOField isoField , String value, boolean clean);
}