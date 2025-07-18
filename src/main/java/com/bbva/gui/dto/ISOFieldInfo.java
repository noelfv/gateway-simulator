package com.bbva.gui.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ISOFieldInfo {

    private int id;
    private String name;
    private String value;
    private String typeData;
    private boolean isVariable;
    private int length;
    private String caracteristicaDato;

    public ISOFieldInfo() {

    }

    public ISOFieldInfo(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public ISOFieldInfo(int id, String name, String value, String typeData, int length, boolean isVariable) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.typeData = typeData;
        this.isVariable = isVariable;
        this.length = length;
    }

    public ISOFieldInfo(int id, String name, String typeData, boolean isVariable, int length) {
        this.id = id;
        this.name = name;
        this.typeData = typeData;
        this.isVariable = isVariable;
        this.length = length;
    }

    public ISOFieldInfo(int id, String name, String typeData, String caracteristicaDato, int length) {
        this.id = id;
        this.name = name;
        this.typeData = typeData;
        this.caracteristicaDato = caracteristicaDato;
        this.length = length;
    }


    @Override
    public String toString() {
        return String.format("ISOFieldInfo{id=%d, name=%s, value=%s, typeData=%s, length=%d, isVariable=%s}",
                id, name, value, typeData, length, isVariable);
    }
}