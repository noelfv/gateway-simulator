package org.example.orchestrator.common;


public class DataTypeISO8583 {

    private  int id;
    private  String name;
    private  String typeData;
    private  boolean isVariable;
    private  int length;
    private  String caracteristicaDato;

    public DataTypeISO8583() {

    }

    public DataTypeISO8583(int id, String name, String typeData, boolean isVariable, int length) {
        this.id = id;
        this.name = name;
        this.typeData = typeData;
        this.isVariable = isVariable;
        this.length = length;
    }

    public DataTypeISO8583(int id, String name, String typeData, String caracteristicaDato, int length) {
        this.id = id;
        this.name = name;
        this.typeData = typeData;
        this.caracteristicaDato = caracteristicaDato;
        this.length = length;
    }


    public String getCaracteristicaDato() {
        return caracteristicaDato;
    }

    public void setCaracteristicaDato(String caracteristicaDato) {
        this.caracteristicaDato = caracteristicaDato;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeData() {
        return typeData;
    }

    public void setTypeData(String typeData) {
        this.typeData = typeData;
    }

    public boolean isVariable() {
        return isVariable;
    }

    public void setVariable(boolean variable) {
        isVariable = variable;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
